# Taika

A Clojure wrapper around the Firebase REST API.

**Note that this is not a Firebase client in Clojure, it's just the REST API**

## Usage

Taika tries to stay close to the wording used in the [Firebase REST Documentation](https://www.firebase.com/docs/rest-api.html). Make sure that either 1.) you've read the security documentation and have configured things appropriately, or 2.) have read/write access turned on for everything while testing. Security will likely be the biggest pain while getting started.

Add Taika to you project

```clojure
    (require '[com.zenboxapp/taika :as taika])
```

Create a token generator and an auth token:

```clojure
    (def user-auth-token
      (let [token-generator (taika/token-generator "SECRET-KEY")
            auth-data {:username "taika" :team_id 100}
            admin? false]
        (taika/create-token token-generator auth-data admin?)))
```
See the [Custom Token Generation](https://www.firebase.com/docs/security/custom-login.html) to read more about the token structure, and the [Security Auth Variable](https://www.firebase.com/docs/security/rule-expressions/auth.html) page to understand what to put in the auth-data map for Firebase's [Security Rules](https://www.firebase.com/docs/security/security-rules.html).

Create a new entry in the Firebase database (note we're using 10 as the customer's id/handle here):

```clojure
    (taika/write! "db-name" "/customers" {10 {:name "Samuel Calans"}} user-auth-token)
    ; => {"10" {"name" "Samuel Calans"}}
```

The user-auth-token is optional, and only needed if your security rules require it, for example with reading data:

```clojure
    (taika/read "db-name" "/customers/10")
    ; => {"name" "Samuel Calans"}
```
 
 Update (merge) a given entry:
 
```clojure
     (taika/update! "db-name" "/customers" {10 {:name "Samuel Hayes" :area "SF"}} user-auth-token)
     ; => {"10" {"name" "Samuel Hayes" :area "SF"}} 
```
     
 Destructively update (replace) an entry:
 
```clojure
     (taika/write! "db-name" "/customers" {10 {:name "Noah Maranchi"}} user-auth-token)
     ; => {"10" {:name "Noah Maranchi"}}
```
 
Destroy data:

```clojure
    (taika/destroy! "db-name" "/customers/10" user-auth-token)
    ; => nil
```

Finally, you can update the security rules from Taika as well. Given a Clojure map, Taika will replace the **ENTIRE** rule document with it. This method requires your secret key as well. Be careful with this!

```clojure
    (taike/update-rules! "db-name" "SECRET-KEY" {:rules {:customers
                                                          {:write true
                                                           :read true}}})
```

## TODO

Use `clj-http`'s connection pooling to speed up serial requests. Right now it's not too slow, but could be faster.
Write tests - not sure how to approach this for a purely 3rd-party wrapper. Any suggestions/pull-requests readily welcome.

## License

Copyright © 2013 Bushido Inc

Distributed under the Eclipse Public License, the same as Clojure.
