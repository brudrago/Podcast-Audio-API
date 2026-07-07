(ns audio-api.database.connection
  (:require [next.jdbc :as jdbc])
  (:import [com.zaxxer.hikari HikariDataSource]))

(def db-config
  {:jdbcUrl  (System/getenv "DATABASE_URL")
   :username (System/getenv "DATABASE_USER")
   :password (System/getenv "DATABASE_PASSWORD")})

(defonce datasource
         (delay
           (let [ds (HikariDataSource.)]
             (.setJdbcUrl ds (:jdbcUrl db-config))
             (.setUsername ds (:username db-config))
             (.setPassword ds (:password db-config))
             ds)))

(defn get-datasource []
  @datasource)

(defn query [sql-params]
  (jdbc/execute! (get-datasource) sql-params))

(defn query-one [sql-params]
  (jdbc/execute-one! (get-datasource) sql-params))

(defn execute [sql-params]
  (jdbc/execute! (get-datasource) sql-params))