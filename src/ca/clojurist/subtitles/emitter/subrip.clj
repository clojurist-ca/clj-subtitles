(ns ca.clojurist.subtitles.emitter.subrip
  "SubRip format subtitle emitter."
  {:author "Robert Medeiros" :email "robert@clojurist.ca"}
  (:require
   [clojure.string :as string])
  (:require
   [ca.clojurist.subtitles.time :as time]))


(defn ->subtitle
  [subtitle]
  (let [{:keys [index start-ms end-ms lines]} subtitle
        start-time (time/ms->duration start-ms :decimal \,)
        end-time (time/ms->duration end-ms :decimal \,)
        duration (str start-time  " --> " end-time)]
    (into [(str index) duration]
          ;; Append an empty line to separate subtitles.
          (conj lines ""))))

(defn export-subrip
  "Return a string containing subtitles in SubRip format."
  [subtitles]
  {:pre [(vector? subtitles) (every? map? subtitles)]}
  (string/join "\n" (mapcat ->subtitle subtitles)))
