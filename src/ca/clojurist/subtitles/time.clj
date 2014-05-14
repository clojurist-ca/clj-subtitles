(ns ca.clojurist.subtitles.time
  "Utilities for working with time"
  {:author "Robert Medeiros" :email "robert@clojurist.ca"}
  (:require
   [clojure.string :as string]))

(defn ms->s
  "Convert milliseconds into seconds and a remainder."
  [^long ms]
  {:pre [(or (zero? ms) (pos? ms))]}
  [(quot ms 1000) (rem ms 1000)])

(defn s->ms
  "Convert seconds into milliseconds."
  [^long s]
  {:pre [(or (zero? s) (pos? s))]}
  (* s 1000))

(defn ms->m
  "Convert milliseconds into minutes and a remainder."
  [^long ms]
  {:pre [(or (zero? ms) (pos? ms))]}
  [(quot ms (* 1000 60)) (rem ms (* 1000 60))])

(defn m->ms
  "Convert minutes into milliseconds."
  [^long m]
  {:pre [(or (zero? m) (pos? m))]}
  (* m 60 (s->ms 1)))

(defn ms->h
  "Convert milliseconds into hours and a remainder."
  [^long ms]
  {:pre [(or (zero? ms) (pos? ms))]}
  [(quot ms (* 1000 60 60)) (rem ms (* 1000 60 60))])

(defn h->ms
  "Convert hours into milliseconds."
  [^long h]
  {:pre [(or (zero? h) (pos? h))]}
  (* h 60 (m->ms 1)))

(defn ms->duration
  "Convert milliseconds into a duration string. To specify the string
  used to separate the hours, minutes, and seconds field pass a string
  as the keyword parameter :separator. Specify the decimal point
  string as the :decimal parameter."
  [^Integer ms & {:keys [separator decimal]
                  :or {separator ":" decimal "."}}]
  {:pre [(or (zero? ms) (pos? ms))]}
  (let [[h r] (ms->h ms)
        [m r] (ms->m r)
        [s r] (ms->s r)
        ms r
        h+m+s (->> (repeat 3 "%02d")
                   (string/join separator))
        h+m+s+ms (str h+m+s decimal "%03d")]
    (format h+m+s+ms h m s ms)))

(defn parse-time
  "Convert a string timestamp containing hours, minutes, seconds, and
  milliseconds into a map. Optional :separator and :decimal keyword
  parameters may be used to specify the expected field separator and
  decimal point character sequences."
  [^String timestamp & {:keys [separator decimal]
                        :or {separator ":" decimal "."}}]
  (letfn [;; Convert a string into an integer.
          (str->int [s] (-> s string/trim Integer/parseInt))
          ;; Define fn taking field separator and decimal point and
          ;; returns a Clojure regex for use with (re-matches).
          (make-time-pattern [separator decimal]
            (let [pattern-hms (->> (repeat 3 "(\\d{2})")
                                   (string/join (str "\\" separator)))
                  pattern-hms+ms (str pattern-hms "\\" decimal "(\\d+){1,3}")]
              (re-pattern pattern-hms+ms)))]
    (if-let [m (re-matches (make-time-pattern separator decimal) timestamp)]
      ;; TODO check the ranges of the parsed integers, e.g. h/m/s
      ;; in [0,59], ms in [0,999].
      (let [[t h m s ms] m
            hours (str->int h)
            minutes (str->int m)
            seconds (str->int s)
            milliseconds (str->int ms)]
        {:hours hours, :minutes minutes, :seconds seconds, :milliseconds milliseconds}))))

(defn time->ms
  "Given a map containing a time duration specified
  as :hours, :minutes, :seconds, and :milliseconds, return the total
  number of milliseconds in the time span."
  [{:keys [hours minutes seconds milliseconds]}]
  {:pre [(every? integer? [hours minutes seconds milliseconds])]
   :post [(number? %)]}
  (let [t [(h->ms hours) (m->ms minutes) (s->ms seconds) milliseconds]]
    (reduce + t)))
