(ns taika.core-test
  (:require [midje.sweet :refer :all]
            [taika.core :as taika]))

; Firebase database URL used in tests
(def url "https://test-db.firebaseIO.com/users.json")

(fact "`db-url` should return a proper Firebase database URL"
  (taika/db-url "test-db" "/users") => url)

(fact "`request` should call client method with correct data"
  (taika/request --method-- "test-db" "/users" {:name "Boobin"}) => {"success" true}
  (provided
    (--method-- url {:query-params {:pretty-print true} :body "{\"name\":\"Boobin\"}"} {:as :json}) => {:body "{\"success\":true}"}))