(defproject audio-api "0.1.0-SNAPSHOT"
  :description "Podcast Audio API"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [ring/ring-core "1.12.2"]
                 [ring/ring-jetty-adapter "1.12.2"]
                 [ring/ring-json "0.5.1"]
                 [compojure "1.7.1"]
                 [cheshire "5.13.0"]
                 [com.github.seancorfield/next.jdbc "1.3.1048"]
                 [org.postgresql/postgresql "42.7.4"]
                 [com.zaxxer/HikariCP "5.1.0"]
                 [migratus "1.5.6"]]
  :main audio-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
