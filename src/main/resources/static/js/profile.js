const app = new Vue({
    el: '#app',
    data: {
        user: '', // username
        psw: '', // password
        pswNew: '', // new password
        pswNew2: '', // new password again
        msg: ''
    },
    mounted() {
        axios.get('/profile')
            .then(function (response) {
                app.user = response.data.user;
            })
            .catch(function (error) {
                console.log(error);
            })
            .then(function () {
                // always executed
            });
    },
    methods: {
        profile() {
            if (this.psw === '') {
                app.msg = '请输入原密码';
                return;
            }
            if (this.pswNew === '') {
                app.msg = '请输入新密码';
                return;
            }
            if (this.pswNew2 === '') {
                app.msg = '请再次输入新密码';
                return;
            }
            if (this.pswNew != this.pswNew2) {
                app.msg = '两次密码不一致';
                return;
            }
            axios.post('/profile', {
                user: this.user,
                psw: this.psw,
                pswNew: this.pswNew
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