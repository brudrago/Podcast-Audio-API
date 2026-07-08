(ns audio-api.handler-test
  (:require
    [clojure.test :refer :all]
    [cheshire.core :as json]
    [audio-api.handler :as handler]
    [audio-api.episodes.service :as service]
    [audio-api.episodes.mapper :as mapper]))

(defn- parse-body [response]
  (json/parse-string (:body response) true))

(deftest health-test
  (testing "returns 200 with ok status"
    (let [response (handler/health {})
          body (parse-body response)]
      (is (= 200 (:status response)))
      (is (= {:status "ok"} body)))))

(deftest get-episodes-test
  (testing "returns mapped episodes"
    (with-redefs [service/list-episodes
                  (fn [] [{:database "episode"}])

                  mapper/episodes->response
                  (fn [_]
                    [{:id "episode-id"
                      :title "Episode"}])]

      (let [response (handler/get-episodes {})
            body (parse-body response)]

        (is (= 200 (:status response)))
        (is (= [{:id "episode-id"
                 :title "Episode"}]
               (:episodes body)))))))

(deftest get-episode-by-id-found-test
  (testing "returns 200 when episode exists"
    (with-redefs [service/get-episode-by-id
                  (fn [id]
                    {:id id})

                  mapper/episode->response
                  (fn [episode]
                    {:id (:id episode)
                     :title "Episode"})]

      (let [response
            (handler/get-episode-by-id
              "44444444-4444-4444-4444-444444444455")

            body (parse-body response)]

        (is (= 200 (:status response)))

        (is (= "44444444-4444-4444-4444-444444444455"
               (:id body)))

        (is (= "Episode"
               (:title body)))))))

(deftest get-episode-by-id-not-found-test
  (testing "returns 404 when episode does not exist"
    (with-redefs [service/get-episode-by-id
                  (fn [_] nil)]

      (let [response
            (handler/get-episode-by-id
              "44444444-4444-4444-4444-444444444456")

            body (parse-body response)]

        (is (= 404 (:status response)))

        (is (= "Episode not found"
               (:error body)))))))

(deftest create-episode-test
  (testing "returns 201 with mapped created episode"
    (with-redefs [mapper/request->episode
                  (fn [body]
                    {:internal body})

                  service/create-episode
                  (fn [episode]
                    {:created episode})

                  mapper/episode->response
                  (fn [_]
                    {:id "created-id"
                     :title "Created Episode"})]

      (let [request {:body {:title "Created Episode"}}
            response (handler/create-episode request)
            body (parse-body response)]

        (is (= 201 (:status response)))

        (is (= "created-id"
               (:id body)))

        (is (= "Created Episode"
               (:title body)))))))