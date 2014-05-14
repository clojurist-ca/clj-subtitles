(ns ca.clojurist.subtitles.core
  ""
  {:author "Robert Medeiros" :email "robert@clojurist.ca"})


(defn convert
  "Convert one subtitle format into another."
  [import-fn export-fn transforms sub-string]
  {:pre [(string? sub-string)
         (every? fn? [import-fn export-fn])
         (vector? transforms) (every? fn? transforms)]}
  (let [transform-fn (apply comp transforms)]
    (-> sub-string import-fn transform-fn export-fn)))
