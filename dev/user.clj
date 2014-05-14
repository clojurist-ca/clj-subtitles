(ns user
  "Tools for interactive development at the REPL. This file should not
  be included in a production build of the application."
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer (javadoc)]
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer :all]
   [clojure.set :as set]
   [clojure.string :as string]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [clojure.tools.trace :refer :all])
  (:require
   [ca.clojurist.subtitles.core :as subtitles]
   [ca.clojurist.subtitles.emitter.subrip :as srt-out]
   [ca.clojurist.subtitles.ingester.subrip :as srt-in]
   [ca.clojurist.subtitles.time :as time]
   [ca.clojurist.subtitles.transform :as transform]))

(def system
  "A Var containing an object representing the application under
  development."
  nil)

(defn init
  "Creates and initializes the system under development in the Var
  #'system."
  []
  ;; TODO
  )

(defn start
  "Starts the system running, updates the Var #'system."
  []
  ;; TODO
  )

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  ;; TODO
  )

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (refresh :after 'user/go))
