<script setup>
  import {ref} from 'vue'
  let hoverIndex = ref(null)
  let menuInfo = [
    {name:'商店'},
    {name:'社区'},
    {name:'关于'},
    {name:'客服'}
  ]
  const inputInfo = [
    {info:[{name:'主页'},{name:'探索队列'},{name:'愿望单'},{name:'点数商店'},{name:'新闻'},{name:'统计数据'}]},
    {info:[{name:'主页'},{name:'讨论'},{name:'倡议攻防'},{name:'市场'},{name:'实况直播'}]}
  ]
  const showMenu = (index) => {
    hoverIndex = index
    // document.querySelector('.display').style.display = 'block'
  }
  const closeMenu = (index) => {
    hoverIndex = index
    // document.querySelector('.display').style.display = 'none'
  }
  const show = (index) => {
    if(index <2){
      return true;
    }
  }
</script>
<!--@mouseleave="closeMenu"-->
<!-- :style="{display:hoverIndex === index ? block : none}"-->
<template>
  <div id="headNavbar">
    <img src="/steamLoge.png" alt="" id="loge">
    <ul id="Input">
      <li v-for="(item,index) in menuInfo" id="li">
        <div id="input" class="left" @mouseenter="showMenu(index)">{{item.name}}</div>
        <div id="menu" class="display" v-if="show(index)" :style="{display: hoverIndex === index ? 'block' : 'none'}">
        <ul>
            <li v-for="(item) in inputInfo[index].info">
              <div id="menuStyle">{{item.name}}</div>
            </li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</template>

<style scoped>
  *{
    padding: 0;
    margin: 0;
  }
  ul{
    list-style-type: none;
  }
  .display{
    display: none;
  }
  #headNavbar{
    width: 940px;
    height: 104px;
    background: #171d25;
  }
  #loge{
    height:50px;
    width:auto;
    margin-top:26px;
  }
  #Input{
    margin-bottom:12px;
    display: inline-block;
    padding-left:10px;
    #li{
      padding-right:10px;
      float: left;
      color:white;
    }
  }
  #menu{
    width:65px;
    position: absolute;
  }
  #menuStyle{
    background: #3d4450;
    font-size:14px;
    width:65px;
    padding:6px;
    color:#dcdedf;
    cursor: pointer;
  }
  #menuStyle:hover{
    background: #dcdedf;
    color:#3d4450;
    cursor: pointer;
  }
</style>