import Vue from 'vue'
import App from './App.vue'
import makeDb from "./services/tracker"
import store from "./store/store"
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import './assets/bootstrap.scss'


Vue.use(IconsPlugin)
Vue.config.productionTip = false;

Vue.use(BootstrapVue);

new Vue({
  store,
  db: makeDb(),
  render: h => h(App),
}).$mount('#app');

