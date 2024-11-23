<script setup>

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

  /**
   * 当鼠标悬浮在标签上，运行该方法
   */
  const showMenu = (index) => {
    document.querySelector(`#a${index}`).style.display = 'block'
  }
  /**
   * 当鼠标离开标签后，运行该方法
   */
  const closeMenu = (index) => {
    document.querySelector(`#a${index}`).style.display = 'none'
  }

  const show = (index) => {
    if(index <2){
      return true;
    }
  }
</script>

<template>
  <div id="headNavbar">
    <img src="/favicon.ico" alt="" id="loge">
    <ul id="Input">
      <li v-for="(item,index) in menuInfo" id="li" :key="item.id">
        <!-- 鼠标悬浮在该地方，就会让下面的下拉列表隐藏和显示 -->
        <div id="input" class="left" @mouseenter="showMenu(index)" @mouseleave="closeMenu(index)">{{item.name}}</div>

        <!-- 这是下拉显示的列表 -->
        <div :id="'a'+index" class="display" v-if="show(index)" @mouseenter="showMenu(index)" @mouseleave="closeMenu(index)">
        <ul>
            <li v-for="(item) in inputInfo[index].info" :key="item.id">
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
    width:65px;
    position: absolute;
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