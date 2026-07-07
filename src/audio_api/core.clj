(ns audio-api.core
  (:require
    [ring.adapter.jetty :refer [run-jetty]]
    [ring.middleware.json :refer [wrap-json-body]]
    [audio-api.routes :as routes]
    [audio-api.middleware.error :refer [wrap-error-handling]]))

(def app
  (-> routes/app-routes
      (wrap-json-body {:keywords? true})
      wrap-error-handling))

(defn -main [& _args]
  (println "Server running on port 3000")
  (run-jetty app {:port 3000 :join? false}))