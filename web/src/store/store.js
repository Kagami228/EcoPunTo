import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    state:[],
  
  },
  mutations: {
    setState(state,data){
      state.state=data;

    }
  
  },
  
  actions: {

  }
})
