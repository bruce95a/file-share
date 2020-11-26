const app = new Vue({
    el: '#app',
    data: {
        file: null,
        msg: ''
    },
    methods: {
        selectFile(event) {
            this.file = event.target.files[0]
        },
        upload() {
            let formData = new FormData();
            formData.append('file', this.file);
            axios.post('/upload', formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            }).then(function (response) {
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
