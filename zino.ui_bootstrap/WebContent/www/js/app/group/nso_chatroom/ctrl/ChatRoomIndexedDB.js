define([], function () {
	const PrjIndexedDBController    = function () {
        const self                  = this;

		const dbName                = "indexedDBChatRoom"
        const collection            = "messages"

        let pr_db                   = null

        this.do_lc_init = () => {
            const req   = window.indexedDB.open(dbName, 1);

            req.onsuccess = e => {
                pr_db = req.result;
                self.do_lc_show();
            };
          
            req.onupgradeneeded = e => {
              pr_db = e.target.result;
              pr_db.createObjectStore(collection);
            };
        }

        this.do_lc_show = (id, code, type = "mem") => {
            // self.do_lc_update_messages(id, code, type, [pr_futured_messages])

            // self.do_lc_req_messages(id, code, type)
        }

        this.do_lc_req_messages = async (id, code, type = "mem") => {
            if(!id || !code || !type) return
            
            let pr_messages = {};
            const objStore = pr_db.transaction(collection).objectStore(collection);

            const objStoreGet = objStore.get(`${id}|${code}|${type}`)

            pr_messages = await new Promise(resolve => {
                objStoreGet.onsuccess = (e) => {
                    return resolve(objStoreGet.result?.content || {})
                }
            })

            return pr_messages
        }

        this.do_lc_update_messages = (id, code, type = "mem", messages = []) => {
            if(!id || !code || !type || !messages || messages.length <= 0) return

            const objStore = pr_db
            .transaction(collection, "readwrite")
            .objectStore(collection)
            
            objStore.put({
                content: {
                    dt: req_gl_DateStr_From_DateObj(messages[0].dt01, 'yyyy-MM-dd HH:mm:ss'),
                    data: messages
                },
                completed: false,
            }, `${id}|${code}|${type}`)
        }

        this.do_lc_delete_messages = (id, code, type) => {
            if(!id || !code || !type) return

            const objStore = pr_db.transaction(collection).objectStore(collection);

            objStore.delete(`${id}|${code}|${type}`)
        }

        this.do_lc_clear_messages = () => {
            if(App.data.user) return

            pr_db.transaction(collection).objectStore(collection).clear();
        }
	};

	return PrjIndexedDBController;
});