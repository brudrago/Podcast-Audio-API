(ns audio-api.app-test
  (:require
    [clojure.test :refer :all]
    [cheshire.core :as json]
    [ring.mock.request :as mock]
    [audio-api.core :refer [app]]
    [audio-api.episodes.service :as service]
    [audio-api.episodes.mapper :as mapper]))

(defn- parse-body [response]
  (json/parse-string (:body response) true))

(deftest health-route-test
  (testing "GET /bd-audio returns 200"
    (let [request (mock/request :get "/bd-audio")
          response (app request)
          body (parse-body response)]

      (is (= 200 (:status response)))
      (is (= {:status "ok"} body)))))


(deftest route-not-found-test
  (testing "returns 404 for unknown route"
    (let [request (mock/request :get "/unknown")
          response (app request)
          body (parse-body response)]

      (is (= 404 (:status response)))
      (is (= "Route not found" (:error body))))))


(deftest get-episodes-route-test
  (testing "GET /bd-audio/episodes returns episodes"
    (with-redefs [service/list-episodes
                  (fn []
                    [{:database "episode"}])

                  mapper/episodes->response
                  (fn [_]
                    [{:id "episode-id"
                      :title "Episode"}])]

      (let [request
            (mock/request :get "/bd-audio/episodes")

            response
            (app request)

            body
            (parse-body response)]

        (is (= 200 (:status response)))

        (is (= [{:id "episode-id"
                 :title "Episode"}]
               (:episodes body)))))))