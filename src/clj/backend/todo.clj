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

    ;; 全部客户端的同步批量操作:
    
    ;; 已经连接的客户端列表: (prn (:connected-uids sente/sente)) => {:ws #{"aac27ad1-8d27-4d8a-9ef6-de6502cb7eb8" "898377ef-700e-4f20-8f99-f4a7dbf4c921"}, :ajax #{}, :any #{"aac27ad1-8d27-4d8a-9ef6-de6502cb7eb8" "898377ef-700e-4f20-8f99-f4a7dbf4c921"}}
    
    ;; 发送消息的函数send-fn: (:send-fn sente/sente) => #object[taoensso.sente$make_channel_socket_server_BANG_$send_fn__11841 0x5c87c64c "taoensso.sente$make_channel_socket_server_BANG_$send_fn__11841@5c87c64c"]

    ;; 发送消息给某个客户端: 
    ;; 指定某个客户端才发消息: ` ((:send-fn sente/sente) "63939259-88fd-4682-bde5-885a65055dba" [:todo/push-all {:todos ["abc" "ooo"]}]) `
    ;; Broadcast new todo list to everyone
    (doseq [uid (:any @(:connected-uids sente/sente))]
      ((:send-fn sente/sente)
       uid
       [:todo/push-all {:todos @db}]))))
