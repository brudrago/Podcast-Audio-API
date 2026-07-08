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

(deftest get-episode-by-id-route-test
  (testing "GET /bd-audio/episode/:id returns episode"
    (with-redefs [service/get-episode-by-id
                  (fn [id]
                    {:id id})

                  mapper/episode->response
                  (fn [episode]
                    {:id (:id episode)
                     :title "Episode"})]

      (let [id "44444444-4444-4444-4444-444444444460"
            request (mock/request :get
                                  (str "/bd-audio/episode/" id))
            response (app request)
            body (parse-body response)]

        (is (= 200 (:status response)))
        (is (= id (:id body)))
        (is (= "Episode" (:title body)))))))


(deftest get-episode-by-id-not-found-route-test
  (testing "GET /bd-audio/episode/:id returns 404 when episode does not exist"
    (with-redefs [service/get-episode-by-id
                  (fn [_] nil)]

      (let [request
            (mock/request
              :get
              "/bd-audio/episode/44444444-4444-4444-4444-444444444461")

            response (app request)
            body (parse-body response)]

        (is (= 404 (:status response)))
        (is (= "Episode not found" (:error body)))))))


(deftest create-episode-route-test
  (testing "POST /bd-audio/episodes returns 201"
    (with-redefs [mapper/request->episode
                  (fn [body]
                    {:internal body})

                  service/create-episode
                  (fn [episode]
                    {:created episode})

                  mapper/episode->response
                  (fn [_]
                    {:id "44444444-4444-4444-4444-444444444462"
                     :title "Created Episode"})]

      (let [request-body
            {:id "44444444-4444-4444-4444-444444444462"
             :title "Created Episode"
             :description "Episode created from route test"
             :durationSeconds 300
             :audioKey "episodes/6/audio.mp3"
             :publishedAt "2026-07-07T20:00:00"}

            request
            (-> (mock/request :post "/bd-audio/episodes")
                (mock/json-body request-body))

            response (app request)
            body (parse-body response)]

        (is (= 201 (:status response)))
        (is (= "44444444-4444-4444-4444-444444444462"
               (:id body)))
        (is (= "Created Episode"
               (:title body)))))))

(deftest get-episode-with-invalid-uuid-route-test
  (testing "GET /bd-audio/episode/:id returns 400 for invalid UUID"
    (let [request (mock/request
                    :get
                    "/bd-audio/episode/invalid-id")

          response (app request)
          body (parse-body response)]

      (is (= 400 (:status response)))

      (is (= "Invalid UUID"
             (:error body))))))


(deftest create-episode-with-empty-title-route-test
  (testing "POST /bd-audio/episodes returns 400 when title is empty"
    (let [request-body
          {:id "44444444-4444-4444-4444-444444444463"
           :title ""
           :description "Invalid episode"
           :durationSeconds 300
           :audioKey "episodes/invalid/audio.mp3"
           :publishedAt "2026-07-07T20:00:00"}

          request
          (-> (mock/request :post "/bd-audio/episodes")
              (mock/json-body request-body))

          response (app request)
          body (parse-body response)]

      (is (= 400 (:status response)))

      (is (= "Title is required"
             (:error body)))

      (is (= "title"
             (:field body))))))