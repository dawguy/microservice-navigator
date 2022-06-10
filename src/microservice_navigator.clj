(ns microservice-navigator
  (:require [clojure.string :as str]))

(defn get-current-directory [] (let [path (str/split (.getAbsolutePath (clojure.java.io/file "./")) #"/")]
                                 (first (drop (- (count path) 2) path))))
(defn get-files [] (mapv identity (.list (clojure.java.io/file "./"))))

(def targets ["http://www.github.com/dawguy/aaa" "http://www.github.com/dawguy/bbb" "http://www.github.com/dawguy/ccc"])
(def files ["abca" "1234" "ghjj"])


(def microservice-mapping
  {"abc" "http://www.github.com/dawguy/aaa"
   "def" "http://www.github.com/dawguy/zzz"
   "ghj" "http://www.github.com/dawguy/123"})

(def sub-options
  {"Home" "/"
   "Merge requests" "/pull-request"
   "Environment" "/environment"
   "Pipeline" "/pipeline"})

(defn provide-options [targets]
  (if (empty? targets)
    (prn (str "No valid repo targets found. There were " (count microservice-mapping) " microservices listings checked."))
      (do
        (prn "-----------------------------------------")
        (prn "Select the microservice you wish to open:")
        (prn "1: All")
        (for [i (range 2 (+ (count targets) 2))
              :let [t (get targets (- i 2))]]
          (.write *out* (str i ": " t)))
        (.read *in*)
        )))

(defn provide-sub-options [targets]
  (if (empty? targets)
    (prn "No valid targets found")
    (do
      (prn "-----------------------------------------")
      (prn "Select the page you want to open:")
      (for [i (range 1 (inc (count sub-options)))
            o (keys sub-options)]
        (prn (str i ": " o))))
    ))

(defn matching-microservices [files]
  (let [current-directory (get-current-directory)]
    (if (contains? files current-directory)
      (provide-sub-options [current-directory])
      (provide-options (map #(get microservice-mapping %) (filter #(contains? microservice-mapping %) files))))))

; Idea. Make sure the option for branch is allowed
(defn run [& opts]
  (prn (matching-microservices (get-files)))
  (prn (str "The line you wrote was " (read-line)))
  )

(run)