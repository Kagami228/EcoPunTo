
import Vue from 'vue'
import firebase from 'firebase'


Vue.mixin({
    beforeCreate() {
        const options = this.$options;
        if (options.db)
            this.$db = options.db;
        else if (options.parent && options.parent.$db)
            this.$db = options.parent.$db;
    }
});

export default function makeDb() {
    var config = {
        apiKey: "AIzaSyCy2W86rO0zV4v5gcoXOYZCMCkqHb6jsJs",
        authDomain: "eco-punto.firebaseapp.com",
        databaseURL: "https://eco-punto.firebaseio.com",
        projectId: "eco-punto",
        storageBucket: "eco-punto.appspot.com",
        messagingSenderId: "404971762473",
        appId: "1:404971762473:web:ac62dacc9dca30ca97fd38"
    };
    firebase.initializeApp(config);
    return firebase.firestore();
}
