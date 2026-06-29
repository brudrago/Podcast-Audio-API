(defproject audio-api "0.1.0-SNAPSHOT"
  :description "Podcast Audio API"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [ring/ring-core "1.12.2"]
                 [ring/ring-jetty-adapter "1.12.2"]
                 [compojure "1.7.1"]
                 [cheshire "5.13.0"]]
  :main audio-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
