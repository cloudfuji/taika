(ns taika.auth
  (:require [clojure.string :as string]
            [clj-http.client :as client]
            [cheshire.core :as json])
  (:import [com.firebase.firebase-token-generator.security.token]
           [org.json.JSONOBject]))

(defn token-generator [secret-key]
  (com.firebase.security.token.TokenGenerator. secret-key))

(defn create-token [token-generator auth-data & [admin?]]
  (let [token-options (doto (com.firebase.security.token.TokenOptions.)
                        (.setAdmin (or admin? false)))
        auth-json (org.json.JSONObject. (java.util.HashMap. auth-data))]
    (.createToken token-generator auth-json token-options)))
