const app = new Vue({
    el: '#app',
    data: {
        reports: [{
            uuid: '',
            name: '',
            datetime: '',
            last: ''
        }],
        page: 1, // page index
        size: 10, // page size
        total: 1, // total page
        count: 0 // total count
    },
    mounted() {
        axios.get('/reports', {
            params: {
                page: this.page,
                size: this.size
            }
        })
            .then(function (response) {
                app.setData(response);
            })
            .catch(function (error) {
                console.log(error);
            })
            .then(function () {
                // always executed
            });
    },
    methods: {
        next(arg) {
            let page = app.page + arg;
            page = page > 0 ? page : 1;
            app.goto(page);
        },
        goto(page) {
            app.page = page;
            axios.get('/reports', {
                params: {
                    page: app.page,
                    size: app.size
                }
            })
                .then(function (response) {
                    app.setData(response);
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        setData(response) {
            app.reports = response.data.reports;
            app.count = response.data.count;
            app.total = app.count % app.size == 0 ? (app.count / app.size) : (parseInt(app.count / app.size) + 1);
            app.total = app.total == 0 ? 1 : app.total;
        },
        rescan() {
            axios.get('/rescan')
                .then(function (response) {
                    app.goto(1);
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        upload() {
            window.location.pathname = '/upload.html';
        },
        del(uuid) {
            axios.delete('/reports/' + uuid)
                .then(function (response) {
                    app.goto(app.page);
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        },
        logout() {
            axios.get('/logout')
                .then(function (response) {
                    window.location.pathname = '/';
                })
                .catch(function (error) {
                    console.log(error);
                })
                .then(function () {
                    // always executed
                });
        }
    }
});

// Script to open and close sidebar
function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("myOverlay").style.display = "block";
}

function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("myOverlay").style.display = "none";
}