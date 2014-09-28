(ns taika.core
  (:require [clojure.string :as string]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [taika.auth :as auth])
  (:refer-clojure :exclude [read]))

(defn recursive-merge
  "Recursively merge hash maps."
  [a b]
  (if (and (map? a) (map? b))
    (merge-with recursive-merge a b)
    (if (map? a) a b)))

(def firebase-tld "firebaseIO.com")

(defn db-base-url [db-name]
  "Returns a proper Firebase base url given a database name"
  (str "https://" db-name "." firebase-tld))

(defn db-url [db-name path]
  "Returns a proper Firebase url given a database name and path"
  (str (db-base-url db-name) path ".json"))

(defn request [method db-name path data & [auth options]]
  "Request method used by other functions."
  (let [request-options (reduce recursive-merge [{:query-params {:pretty-print true}}
                                                 (when auth {:query-params {:auth auth}})
                                                 (when (not (nil? data)) {:body (json/generate-string data)})
                                                 options])
        url (db-url db-name path)]
    (-> (method url request-options {:as :json})
        :body
        json/parse-string)))

(defn write! [db-name path data & [auth options]]
  "Creates or destructively replaces data in a Firebase database at a given path"
  (request client/put db-name path data auth options))

(defn update!
  "Updates data in a Firebase database at a given path via destructively merging."
  [db-name path data & [auth options]]
  (request client/patch db-name path data auth options))

(defn push!
  "Appends data to a list in a Firebase db at a given path. See https://www.firebase.com/docs/javascript/firebase/push.html for more information."
 [db-name path data & [auth options]]
  (request client/post db-name path data auth options))

(defn remove! [db-name path & [auth options]]
  "Destroys data from Firebase database at a given path"
  (request client/delete db-name path nil auth options))

(defn read
  "Retrieves data from Firebase database at a given path"
 [db-name path & [auth queury-params options]]
  (request client/get db-name path nil auth (merge {:query-params (or queury-params {})} options)))

(defn update-rules!
  "Updates security rules on Firebase - See https://www.firebase.com/docs/security/security-rules.html for more information.
  WARNING: Completely replaces existing security rules."
 [db-name secret-key rule-update]
  (client/put (db-url db-name "/.settings/rules") {:query-params {:auth secret-key}
                                                   :body (json/generate-string rule-update {:pretty true})}))
