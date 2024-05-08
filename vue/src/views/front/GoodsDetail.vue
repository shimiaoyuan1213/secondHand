<template>
  <div style="margin: 0 auto; padding: 10px 0; width: 50%">
    <div style="display: flex; grid-gap: 20px; margin-bottom: 40px">
      <img :src="goods.img" alt="" style="width: 50%; height: 400px; display: block">
      <div>
        <div style="font-weight: bold; font-size: 26px; margin: 20px 0">{{ goods.name }}</div>
        <div style="color: #666; font-size: 16px;">
          <span>浏览 {{ goods.readCount }}</span>
          <span style="margin-left: 20px">点赞 {{ goods.likesCount }}</span>
          <span style="margin-left: 20px">收藏 {{ goods.collectCount }}</span>
        </div>
        <div style="color: red; font-size: 24px; margin: 40px 0">￥{{ goods.price }}</div>
        <div style="margin-bottom: 20px"><span style="color: #666">发货地：</span> {{ goods.address }}</div>
        <div style="margin-bottom: 20px"><span style="color: #666">卖家：</span> {{ goods.userName }}</div>
        <div style="margin-bottom: 40px"><span style="color: #666">发布日期：</span> {{ goods.date }}</div>
        <div>
          <el-button v-if="!goods.userLikes" @click="addLikes" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">点赞</el-button>
          <el-button v-if="goods.userLikes" @click="addLikes" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">已点赞</el-button>
          <el-button v-if="!goods.userCollect" @click="addCollect" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">收藏</el-button>
          <el-button v-if="goods.userCollect" @click="addCollect" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">已收藏</el-button>
          <el-button size="medium" type="danger" @click="handleBuy">立即购买</el-button>
        </div>
      </div>
    </div>

    <div>
      <div style="display: flex; border-bottom: 1px solid orangered; margin-bottom: 10px">
        <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品详情' }" @click="changeItem('商品详情')">商品详情</div>
        <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品评论' }" @click="changeItem('商品评论')">商品评论</div>
      </div>

      <div v-if="current === '商品详情'">
        <div v-html="goods.content"></div>
      </div>

      <div v-if="current === '商品评论'">

      </div>
      <el-dialog title="选择收货地址" :visible.sync="fromVisible1" width="30%" :close-on-click-modal="false" destroy-on-close>
        <div style="padding: 0 20px">
          <el-radio-group v-model="form.addressId">
            <el-radio v-for="item in addressList" :key="item.id" :label="item.id" style="margin-bottom: 10px">
              {{ item.name + ' ' + item.address + ' ' + item.phone }}
            </el-radio>

          </el-radio-group>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible1=false">取 消</el-button>>
          <el-button type="primary" @click="addOrders">确 定</el-button>>
        </div>
      </el-dialog>


    </div>
  </div>
</template>

<script>
export default {
  name: "GoodsDetail",
  data() {
    return {
      id: this.$route.query.id,
      goods: {},
      current: '商品详情',
      form: {},
      fromVisible1: false,
      addressList: [],


    }
  },
  created() {
    // 先更新浏览数
    this.$request.put('/goods/updateReadCount/' + this.id).then(res => {
      this.load()
    })

    this.loadAddress()
  },
  methods: {

    addOrders() {
      this.form.goodsId=this.id
      this.$request.post('/orders/add',this.form).then(res => {

        if (res.code==='200' ) {
          console.log('add Data:', res.data); // 这将显示data属性的实际内容
          this.$message.success("下单成功")
          this.$router.push('/front/orders')
        } else {
          // 处理错误或数据不存在的情况
          console.error('No  found');
        }
      }).catch(error => {
        // 处理请求错误
        console.error('Request failed', error);
      });
    },


    loadAddress() {
      this.$request.get('/address/selectAll').then(res => {
        console.log('Response:', res);
        if (res && res.data) {
          console.log('Address Data:', res.data); // 这将显示data属性的实际内容
          this.addressList = res.data;
        } else {
          // 处理错误或数据不存在的情况
          console.error('No address data found');
        }
      }).catch(error => {
        // 处理请求错误
        console.error('Request failed', error);
      });
    },
    handleBuy(){
      this.form={}
      this.fromVisible1=true

    },
    addLikes() {
      this.$request.post('/likes/add', {fid: this.goods.id, module: 'goods'}).then(res => {
        if (res.code === '200') {

          this.load()
        } else {
          this.$message.error(res.msg)
        }


      })
    },
    addCollect(){
      this.$request.post('/collect/add',{fid: this.goods.id,module: 'goods'}).then(res=>{
        if(res.code==='200'){

          this.load()
        }else{
          this.$message.error(res.msg)
        }


      })

    },
    changeItem(current) {
      this.current = current
    },
    load() {
      this.$request.get('/goods/selectById/' + this.id).then(res => {
        this.goods = res.data || {}
      })
    }

}
}
</script>

<style scoped>
.active {
  background-color: orangered;
  color: #eee;
}
</style>