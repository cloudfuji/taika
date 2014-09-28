(defproject com.zenboxapp/taika "0.1.4"
  :description "A wrapper around the Firebase REST API"
  :url "http://www.github.com/cloudfuji/taika"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-http "1.0.0"]
                 [cheshire "5.3.1"]
                 [com.firebase/firebase-token-generator "1.0.2"]
                 [midje "1.6.3"]]
  :plugins [[lein-midje "3.0.0"]])