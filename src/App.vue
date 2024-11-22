<script setup>
  import HeadNavbar from './components/headNavbar.vue'
</script>

<template>
  <div id="div">
    <div id="ddiv">
      <div id="head">
        <div id="headNavbar">
          <HeadNavbar>

          </HeadNavbar>
        </div>
      </div>
      <div id="body">

      </div>
      <div id="feet">

      </div>
    </div>
  </div>


</template>

<style scoped>
  #div{
    width: 100%;
    height: 100%;
    border:1px solid black;
  }
  #ddiv{
    width:940px;
    height:100%;
    border:1px solid #00a67d;
    margin:auto;
  }
  #head {
    height:104px;
    width:940px;
    border:1px solid #df3079;
  }
  #body{
    height:5100px;
    width:940px;
    border:1px solid #df3079;
  }
</style>
