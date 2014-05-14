(ns ca.clojurist.subtitles.transform
  ""
  {:author "Robert Medeiros" :email "robert@clojurist.ca"})

(defn remove-empty-subtitles
  "Given a vector of maps representing subtitles, only keep those that
  have one or more lines of text contained in the map's :lines
  value (a vector)."
  [subtitles]
  {:pre [(vector? subtitles) (every? map? subtitles)]
   :post [(vector? %) (every? map? %)]}
  (filterv #(-> % :lines not-empty) subtitles))

(defn merge-repeated-subtitles
  "Given a vector of maps representing subtitles, look for and merge
  subtitles that have the same text, but where the end time of one is
  the start time of the next."
  [subtitles]
  {:pre [(vector? subtitles) (every? map? subtitles)]
   :post [(vector? %) (every? map? %)]}
  (let [merge-subtitles
        (fn [subtitles next-subtitle]
          (let [subtitle (peek subtitles)]
            (if (and
                 (not-empty subtitles)
                 (= (:lines subtitle) (:lines next-subtitle))
                 (= (:end-ms subtitle) (:start-ms next-subtitle)))
              (conj (pop subtitles)
                    (assoc subtitle :end-ms (:end-ms next-subtitle)))
              (conj subtitles next-subtitle))))]
    (reduce merge-subtitles [] subtitles)))

(defn renumber-subtitles
  "Given a vector of maps representing subtitles, renumber them with
  successive integers starting at the given start-no. The integer
  index of the subtitle will be set on the :index key overwriting it
  if it is already set."
  [start-no subtitles]
  {:pre [(integer? start-no)
         (vector? subtitles)
         (every? map? subtitles)]
   :post [(vector? %) (every? map? %)]}
  (let [renumber-fn (fn [idx subtitle]
                      (assoc subtitle :index (+ start-no idx)))]
    (into [] (map-indexed renumber-fn subtitles))))
