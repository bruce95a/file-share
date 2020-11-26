const app = new Vue({
    el: '#app',
    data: {
        msg: '',
        user: '',
        psw: ''
    },
    methods: {
        login() {
            axios.post('/login', {
                user: this.user,
                psw: this.psw
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
