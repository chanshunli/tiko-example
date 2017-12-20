(ns backend.todo
  (:require [backend.mount.sente :as sente]))

;; Atom as in memory-database.
;; In real life, you would replace this with database connection using Mount.

;; (swap! db conj "测试22")
;; cider里面修改db之后,连接的客户端无法更新, 只有客户端再新增的时候,cider添加的数据才能显示出来
(defonce db (atom []))

;; events.cljs: `:todo/fetch-all`是客户端关注的事件名,更新所有的todo
;; => dispatch `:todo/fetch-all` 事件
;; (re-frame/reg-event-fx
;;  :chsk/handshake
;;  (fn [_ _]
;;    {:dispatch [:todo/fetch-all]}))
;; => 注册 `:todo/fetch-all`事件
;; (re-frame/reg-event-fx
;;  :todo/fetch-all
;;  (fn [_ [_ message]]
;;    {:sente/event {:event [:todo/fetch-all]
;;                   :dispatch-to [:todo/handle-response]}}))
(defmethod sente/event-msg-handler :todo/fetch-all
  [{:keys [event ?reply-fn]}]
  (?reply-fn {:todos @db}))

;; Exercise 6. Fix function so that it does change the db
;; or broadcast if the created todo item was was empty.
(defmethod sente/event-msg-handler :todo/create
  [{:keys [event]}]
  (let [new-todo (-> event second :new-todo)]
    (swap! db conj new-todo)

    ;; Broadcast new todo list to everyone
    (doseq [uid (:any @(:connected-uids sente/sente))]
      ((:send-fn sente/sente)
       uid
       [:todo/push-all {:todos @db}]))))
