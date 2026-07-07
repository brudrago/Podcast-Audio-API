(ns audio-api.database.migrations
  (:require [migratus.core :as migratus]))

(def config
  {:store :database
   :migration-dir "migrations"
   :db {:dbtype   "postgresql"
        :host     (System/getenv "DATABASE_HOST")
        :port     (Integer/parseInt
                    (or (System/getenv "DATABASE_PORT") "5432"))
        :dbname   (System/getenv "DATABASE_NAME")
        :user     (System/getenv "DATABASE_USER")
        :password (System/getenv "DATABASE_PASSWORD")}})

(defn migrate []
  (migratus/migrate config))

(defn rollback []
  (migratus/rollback config))