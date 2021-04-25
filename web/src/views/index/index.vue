<template>
    <div>
      <div class="nowDate" :class="isShadow"><p style="margin-top:0px;margin-bottom:10px">当前时间：{{nowDate}}</p></div>
      <div style="padding:20px"></div>
      <!-- <iframe width="400" scrolling="no" height="100" frameborder="0" allowtransparency="true" src="https://i.tianqi.com?c=code&id=35&icon=1&site=34"></iframe> -->
      <div  :style="{ width: '90%', height: '300px' }" 
          v-for="(mschine,index) in machineInfo" :key="index"
          :id="getID(index)" style="margin-top:30px;margin-left:80px;"></div>
        <transition  name="el-fade-in"><div v-if="showIV"><timeIV @isClosed="closeDeatil" :nodeIV="nodeID" /></div></transition>
    </div>

</template>
<style>
.nowDateShadow{
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.nowDate{
  font-size: 26px;
  font-weight: bold;
  position: fixed;
  z-index: 3;
  left: 0;
  right: 0;
  padding-top: 10px;
  background-color:#9dd4e0;
}
</style>
<script>
import timeIV from '@/components/timeIV'
import axios from 'axios'
export default {
  name: 'index',
  components: {
    timeIV,
  },
  data() {
    return{
      showIV: false,
      machineInfo:null,
      nodeID:null,
      nowDate:"",
      isShadow:'',
    }
  },
  methods:{
    handleScroll () {
      var scrollTop = document.body.scrollTop || document.documentElement.scrollTop
      if (scrollTop > 10) {
        this.isShadow="nowDateShadow";
      }
      else
        this.isShadow="";
    },
    currentTime() {
      setInterval(this.formatDate, 500);
    },
    formatDate() {
      let date = new Date();
      let year = date.getFullYear(); // 年
      let month = date.getMonth() + 1; // 月
      let day = date.getDate(); // 日
      let week = date.getDay(); // 星期
      let weekArr = [ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" ];
      let hour = date.getHours(); // 时
      hour = hour < 10 ? "0" + hour : hour; // 如果只有一位，则前面补零
      let minute = date.getMinutes(); // 分
      minute = minute < 10 ? "0" + minute : minute; // 如果只有一位，则前面补零
      let second = date.getSeconds(); // 秒
      second = second < 10 ? "0" + second : second; // 如果只有一位，则前面补零
      this.nowDate = `${year}/${month}/${day} ${hour}:${minute}:${second} ${weekArr[week]}`;
    },
    getID(index){
      return "chart"+index;
    },
    closeDeatil(flag){
      this.showIV = false;
    },
    warnTransform(machineInfo){
      var timeList=machineInfo.timeList;
      var warnArr=[];
      for(var i=0;i<timeList.length;i++){
        if(timeList[i].warn==0)  warnArr.push("正常");
        if(timeList[i].warn==1)  warnArr.push("开路");
        if(timeList[i].warn==2)  warnArr.push("短路");
        if(timeList[i].warn==3)  warnArr.push("阴影");
      }
      return warnArr;
    },
    timeTransform(machineInfo){
      var timeList=machineInfo.timeList;
      var timeArr=[];
      for(var i=0;i<timeList.length;i++){
          timeArr.push(timeList[i].timeStamp);
      }
      return timeArr;
    },
    initEChart(index){
      this.$nextTick(function(){
      var _this=this;
      var timeArr=_this.timeTransform(this.machineInfo[index]);
      var warnArr=_this.warnTransform(this.machineInfo[index]);
      let myChart = this.$root.echarts.init(
        document.getElementById("chart"+index)
      );
      myChart.on('click',function(params){
        _this.nodeID=_this.machineInfo[index].timeList[params.dataIndex].id;
        console.log(_this.nodeID);
        _this.showIV = true;
      });
      // 绘制图表
      myChart.setOption({
        title: { 
            text: _this.machineInfo[index].machineName,
            left:'center',
            top:20,
            subtext:'当前故障类型：'+warnArr.pop(),
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          show:false,
        },
        xAxis: {
          boundaryGap: true,
          data: timeArr,
        },
        yAxis: {
          boundaryGap: false,
          
          data: [" ","正常","开路","短路","阴影",""],
        },
        series: [
          {
            name: "板1",
            type: "line",
            data: warnArr,
          },
        ],
             dataZoom: [
          {
               type: 'slider',
               show: true,
               xAxisIndex: [0],
               start: 1,
               minSpan:5,
               end: 100
          },
          {
               type: 'inside',
               xAxisIndex: [0],
               start: 1,
               minSpan:5,
               end: 100
          },
     ],
        backgroundColor: 'rgba(255,255,255,0.8)',
      });
    window.onresize = function () {//自适应大小
        myChart.resize();
      };
      });
    },

    getData(){
      var getString='http://127.0.0.1:8080/api/getInfo';
      axios.get(getString).then(response => {
          var data = response.data;
          this.machineInfo=data;
          for(var i=0;i<data.length;i++){
              this.initEChart(i);
          }
      });
    }

  },
  created(){
    this.getData();
  },
  mounted() {
    this.currentTime();
    let _this = this
    window.onscroll = function () {
      _this.handleScroll()
    }
  },
}
</script>

