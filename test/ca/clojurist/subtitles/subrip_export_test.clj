(ns ca.clojurist.subtitles.subrip-export-test
  (:require
   [clojure.string :as string]
   [clojure.test :refer :all])
  (:require
   [ca.clojurist.subtitles.emitter.subrip :refer (export-subrip)]))

(def single-cue {:index 1 :start-ms 1000 :end-ms 2500 :lines ["This is a test"]})

(deftest export-basic-single
  (testing "export of single subtitle"
    (let [subtitles (export-subrip [single-cue])
          lines (string/split-lines subtitles)]
      (is (string? subtitles))
      (is (= 3 (count lines)))
      (is (= (seq ["1" "00:00:01,000 --> 00:00:02,500" "This is a test"]))))))

(deftest export-basic-double
  (testing "export of two subtitles"
    (let [subtitles (-> [single-cue single-cue] export-subrip )
          lines (string/split-lines subtitles)]
      (is (string? subtitles))
      (is (= 7 (count lines))))))
