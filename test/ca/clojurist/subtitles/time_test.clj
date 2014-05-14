(ns ca.clojurist.subtitles.time-test
  (:require
   [clojure.test :refer :all])
  (:require
   [ca.clojurist.subtitles.time :as t]))

(deftest test-ms->s
  (testing "conversion of milliseconds to seconds"
    ;; -n ms -> exception
    (is (thrown? AssertionError (t/ms->s -1)))
    ;; 0 ms = 0 s
    (is (= [0 0] (t/ms->s 0)))
    ;; 1000 ms = 1 s
    (is (= [1 0] (t/ms->s 1000)))))

(deftest test-s->ms
  (testing "conversion of seconds to milliseconds"
    ;; -n s -> exception
    (is (thrown? AssertionError (t/s->ms -1)))
    ;; 0 s -> 0 ms
    (is (= 0 (t/s->ms 0)))
    ;; 1 s -> 1000 ms
    (is (= 1000 (t/s->ms 1)))))

(deftest test-ms->m
  (testing "conversion of milliseconds to minutes"
    ;; -n ms -> exception
    (is (thrown? AssertionError (t/ms->m -1)))
    ;; 0 ms -> 0 m
    (is (= [0 0] (t/ms->m 0)))
    ;; 60,000 ms -> 1 m
    (is (= [1 0] (t/ms->m 60000)))))

(deftest test-m->ms
  (testing "conversion of minutes to milliseconds"
    ;; -n m -> exception
    (is (thrown? AssertionError (t/m->ms -1)))
    ;; 0 m -> 0 ms
    (is (= 0 (t/m->ms 0)))
    ;; 1 m -> 60,000 ms
    (is (= 60000 (t/m->ms 1)))))

(deftest test-ms->h
  (testing "conversion of milliseconds to hours"
    ;; -n ms -> exception
    (is (thrown? AssertionError (t/ms->h -1)))
    ;; 0 ms -> 0 h
    (is (= [0 0] (t/ms->h 0)))
    ;; 3,600,000 ms -> 1 h
    (is (= [1 0] (t/ms->h 3600000)))))

(deftest test-h->ms
  (testing "conversion of hours to milliseconds"
    ;; -1 h -> exception
    (is (thrown? AssertionError (t/h->ms -1)))
    ;; 0 h -> 0 ms
    (is (= 0 (t/h->ms 0)))
    ;; 1 h -> 3,600,000
    (is (= 3600000 (t/h->ms 1)))))

(deftest test-ms->duration
  (testing "conversion of milliseconds to duration string"
    ;; -n ms -> exception
    (is (thrown? AssertionError (t/ms->duration -1)))
    ;; 0 ms -> 0 duration
    (is (= "00:00:00.000" (t/ms->duration 0)))
    ;; 100 ms -> 100 ms
    (is (= "00:00:00.100" (t/ms->duration 100)))
    ;; 1000 ms -> 1 s
    (is (= "00:00:01.000" (t/ms->duration 1000)))
    ;; 60,000 ms -> 1 m
    (is (= "00:01:00.000" (t/ms->duration 60000)))
    ;; 3,600,000 ms -> 1 h
    (is (= "01:00:00.000" (t/ms->duration 3600000)))))

(deftest test-parse-time
  (let [zero-time {:hours 0, :minutes 0, :seconds 0, :milliseconds 0}]
    (testing "parsing of duration strings into maps"
      ;; non-string -> exception
      (is (thrown? ClassCastException (t/parse-time 1)))
      ;; empty string -> nil
      (is (nil? (t/parse-time "")))
      ;; 0:0:0.0 -> nil
      (is (nil? (t/parse-time "0:0:0.0")))
      ;; 00:00:00.000
      (is (= zero-time (t/parse-time "00:00:00.000")))
      ;; 100 ms
      (is (= (merge zero-time {:milliseconds 100}) (t/parse-time "00:00:00.100")))
      ;; 1 s
      (is (= (merge zero-time {:seconds 1}) (t/parse-time "00:00:01.000")))
      ;; 1 m
      (is (= (merge zero-time {:minutes 1}) (t/parse-time "00:01:00.000")))
      ;; 1 h
      (is (= (merge zero-time {:hours 1}) (t/parse-time "01:00:00.000")))
      ;; 1 h, 2 m, 3 s, 4 ms
      (is (= {:hours 1, :minutes 2, :seconds 3, :milliseconds 4} (t/parse-time "01:02:03.004")))
      ;; 1500 ms -> 1 s, 500 ms
      ;; TODO do we accept invalid duration strings and fix them up, or reject them?
      ;;(is (= (merge zero-time {:seconds 1, :milliseconds 500}) (t/parse-time "00:00:00.1500")))
      )))

(deftest test-time->ms
  (let [zero-duration {:hours 0, :minutes 0, :seconds 0, :milliseconds 0}]
    (testing "conversion of duration into millisecond count"
      ;; {0} -> 0 ms
      (is (= 0 (t/time->ms zero-duration)))
      ;; 1 ms -> 1 ms
      (is (= 1 (t/time->ms (merge zero-duration {:milliseconds 1}))))
      ;; 1 s -> 1000 ms
      (is (= 1000 (t/time->ms (merge zero-duration {:seconds 1}))))
      ;; 1 m -> 60,000 ms
      (is (= 60000 (t/time->ms (merge zero-duration {:minutes 1}))))
      ;; 1 h -> 3,600,000
      (is (= 3600000 (t/time->ms (merge zero-duration {:hours 1})))))))
