(ns se.nyrell.dualactivity.second
    (:require [neko.activity :refer [defactivity set-content-view!]]
              [neko.debug :refer [*a]]
              [neko.notify :refer [toast]]
              [neko.intent :as intent]
              [neko.data :refer [like-map]]
              [neko.resource :as res]
              [neko.find-view :refer [find-view]]
              [neko.threading :refer [on-ui]])
    (:import android.widget.EditText
             android.support.v7.app.AppCompatActivity))

;; We execute this function to import all subclasses of R class. This gives us
;; access to all application resources.
(res/import-all)

(defn notify-from-edit
  "Finds an EditText element with ID ::user-input in the given activity. Gets
  its contents and displays them in a toast if they aren't empty. We use
  resources declared in res/values/strings.xml."
  [activity]
  (let [^EditText input (.getText (find-view activity ::user-input))]
    (toast (if (empty? input)
             (res/get-string R$string/input_is_empty)
             (res/get-string R$string/your_input_fmt input))
           :long)))

(defn launch-other-activity
  [a msg]
  (.startActivity a (intent/intent a '.MainActivity {:msg msg})))


(defn page-2 [activity]
  (let [{:keys [msg]} (like-map (.getIntent activity))]
    [:linear-layout {:orientation :vertical}
     [:text-view {:text "Page 2 - SecondActivity"}]
     [:text-view {:text "Message from other page:"}]
     [:text-view {:text (or msg "")}]
     [:edit-text {:id ::user-input
                  :hint "Message to other page"
                  :layout-width :fill}]
     [:button {:text "Switch to page 1"
               ;; :on-click (fn [_] (launch-other-activity activity "Kalle2"))
               :on-click (fn [_] (launch-other-activity
                                  activity
                                  (.getText (find-view activity ::user-input))))
               }]
     ]))


(defactivity se.nyrell.dualactivity.SecondActivity
  :key :second
  :extends AppCompatActivity

  (onCreate [this bundle]
    (.superOnCreate this bundle)
    (neko.debug/keep-screen-on this)
    (on-ui
      (set-content-view! (*a) (page-2 (*a)))
      )))

