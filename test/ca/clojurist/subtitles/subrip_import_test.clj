(ns ca.clojurist.subtitles.subrip-import-test
  (:require
   [clojure.string :as string]
   [clojure.test :refer :all])
  (:require
   [ca.clojurist.subtitles.ingester.subrip :as srt]))

(defn make-cue
  [^Integer idx start-time end-time lines]
  {:pre [(every? vector? [start-time end-time lines])
         (every? string? lines)]}
  (let [fmt (fn [[h m s ms]] (format "%02d:%02d:%02d,%03d" h m s ms))
        start-str (fmt start-time)
        end-str (fmt end-time)
        time-spec (str start-str " --> " end-str)
        text (string/join "\n" lines)]
    (string/join "\n" [idx time-spec text])))

(def cue-start-time [0 0 13 0])
(def cue-start-ms 13000)
(def cue-end-time [0 0 25 500])
(def cue-end-ms 25500)
(def cue-lines ["Test subtitle text"])

(def single-cue (make-cue 1 cue-start-time cue-end-time cue-lines))

(def double-cue
  (let [cue (make-cue 2 [0 0 26 0] [0 0 37 800] ["Second line of text"])]
    (string/join "\n\n" [single-cue cue])))

(deftest import-shape-single
  (testing "imported single subtitle should be a vector of one map"
    (let [subtitles (srt/import-subrip single-cue)]
      (is (vector? subtitles))
      (is (every? map? subtitles))
      (is (= 1 (count subtitles))))))

(deftest import-shape-double
  (testing "imported pair of subtitles should be a vector of two maps"
    (let [subtitles (srt/import-subrip double-cue)]
      (is (vector? subtitles))
      (is (every? map? subtitles))
      (is (= 2 (count subtitles))))))

(deftest import-cue-index-is-integer
  (testing "cue index is an integer"
    (let [cue (-> single-cue srt/import-subrip first)
          index (:index cue)]
    (is (integer? index)))))

(deftest import-cue-has-expected-keys
  (testing "a single cue/subtitle map should have certain keys"
    (let [expected-keys [:index :start-ms :end-ms :lines]
          cue (-> single-cue srt/import-subrip first)]
      (is (every? #(contains? cue %) expected-keys)))))

(deftest import-cue-start-time-in-ms
  (testing "cue start time should in milliseconds"
    (let [start-ms (-> single-cue srt/import-subrip first :start-ms)]
      (is (integer? start-ms))
      (is (= cue-start-ms start-ms)))))

(deftest import-cue-end-time-in-ms
  (testing "cue end time should be in milliseconds"
    (let [end-ms (-> single-cue srt/import-subrip first :end-ms)]
      (is (integer? end-ms))
      (is (= cue-end-ms end-ms)))))

(deftest import-cue-lines-may-be-sequence
  (testing "cue may have a sequence of lines"
    (let [lines (-> single-cue srt/import-subrip first :lines)]
      (is (vector? lines))
      (is (= 1 (count lines)))
      (is (= (first cue-lines) (first lines))))))
