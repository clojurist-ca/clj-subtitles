(defproject ca.clojurist/subtitles "0.1.0-SNAPSHOT"
  :description "Subtitles processing and transformation"
  :url "http://github.com/clojurist-ca/clj-subtitles/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [org.clojure/tools.trace "0.7.9"]]
                   :source-paths ["dev"]}}
  :min-lein-version "2.5.0")
