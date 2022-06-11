(ns microservice-navigator
  (:require [clojure.string :as str]
            [clojure.java.browse :as browse]))

(defn get-current-directory [] (let [path (str/split (.getAbsolutePath (clojure.java.io/file "./")) #"/")]
                                 (first (drop (- (count path) 2) path))))
(defn get-files [] (mapv identity (.list (clojure.java.io/file "./"))))

; Goal for near future is to load this list from SQLite. Goal is to map what the microservice directories are called with
; the source control host's website for them
(def microservice-mapping
  {
   "microservice-navigator" "http://www.github.com/dawguy/microservice-navigator"
   "kelly-criterion" "https://github.com/dawguy/kelly-criterion"
   "kelly-criterion-client" "https://github.com/dawguy/kelly-criterion-client"
   "Rowing" "https://github.com/dawguy/Rowing"
   "rowing-client" "https://github.com/dawguy/Rowing/tree/main/rowing-client"
   })

; Not that many options so these can be hard-coded lists.
; Sub-options describe which function the user wants to load into.
(def gitlab-sub-options [{:label "Todo" :path "/Todo"}])
(def github-sub-options
  [{:label "Home" :path "/"}
   {:label "Issues" :path "/issues"}
   {:label "Pull Requests" :path "/pulls"}
   {:label "Actions" :path "/actions"} ])
(def sub-options github-sub-options)                        ; Swap this out depending on what is being targeted.

;-----------------
; Helpful defs. Will move these to comment once they start being changed less
(def targets ["http://www.github.com/dawguy/aaa" "http://www.github.com/dawguy/bbb" "http://www.github.com/dawguy/ccc"])
(def files ["abc" "1234" "ghj"])
(def files [".nrepl-port" "deps.edn" "microservice-navigator.iml" "LICENSE.txt" ".gitignore" ".idea" "README.md" ".cpcache" ".git" "test" "src"])
(def current-directory (get-current-directory))
(def selected-option "2")
(def matched-microservices (map #(get microservice-mapping %) (filter #(contains? microservice-mapping %) files)))
;-----------------

(defn provide-options [targets]
  (if (empty? targets)
    (prn (str "No valid repo targets found. There were " (count microservice-mapping) " microservices listings checked."))
      (do
        (prn "-----------------------------------------")
        (prn "Select the microservice you wish to open:")
        (println (apply str "1: All\n"
                  (for [i (range 0 (count targets))
                        :let [t (nth targets i)]]
                    (str (+ i 2) ": " t (if (not= (count targets) (inc i)) "\n")))))
        (read-line)
        )))

(defn provide-sub-options [targets]
  (if (empty? targets)
    (prn "No valid targets found")
    (do
      (prn "-----------------------------------------")
      (prn "Select the page you want to open:")
      (println (apply str
                      (for [i (range 0 (count sub-options))
                            :let [o (nth sub-options i)]]
                        (str (inc i) ": " (:label o) (if (not= (count sub-options) (inc i)) "\n")))))
      (let [selected-sub-option (read-line)]
        (if (string? selected-sub-option) (:path (nth sub-options (dec (Integer/parseInt selected-sub-option)))) nil)
        )
      )))

(defn matching-microservices [files]
  (let [current-directory (get-current-directory)]
    (if (contains? microservice-mapping current-directory)
      [[(get microservice-mapping current-directory)] (provide-sub-options [current-directory])]
      (let [matched-microservices (mapv #(get microservice-mapping %) (filter #(contains? microservice-mapping %) files))
            selected-option (provide-options matched-microservices)
            parsed-int (if (string? selected-option) (- (Integer/parseInt selected-option) 2) nil)
            dirs (case selected-option
                   "1" matched-microservices                ; 1 represents all
                   (if (nil? parsed-int) nil (vector (nth matched-microservices parsed-int)))
                   )
            ]
          (if (nil? dirs) nil [dirs (provide-sub-options dirs)])
        )
      )))

(defn gen-list [o]
  (map #(str % (second o)) (first o)))

(defn open-in-browser [s] (browse/browse-url s))

; Idea. Make sure the option for branch is allowed
(defn run [& opts]
  (loop [p (-> (get-files)
               (matching-microservices)
               (gen-list))]
    (if (empty? p) p
                 (do
                   (open-in-browser (first p))
                   (recur (rest p)))))
  (shutdown-agents)
  )

(run)

(comment
  (matching-microservices files)
)
