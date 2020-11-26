const app = new Vue({
    el: '#app',
    data: {
        store: '', // sotre path
        address: '', // site address
        msg: ''
    },
    mounted() {
        axios.get('/config')
            .then(function (response) {
                app.store = response.data.store;
                app.address = response.data.address;
            })
            .catch(function (error) {
                console.log(error);
            })
            .then(function () {
                // always executed
            });
    },
    methods: {
        cfg() {
            axios.post('/config', {
                store: this.store,
                address: this.address
            })
                .then(function (response) {
                    if (response.data.msg == undefined) {
                        window.location.pathname = '/report.html';
                    } else {
                        app.msg = response.data.msg;
                    }
                })
                .catch(function (error) {
                    console.log(error);
                });
        }
    }
});