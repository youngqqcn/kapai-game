<?php

namespace app\admin\controller;

use app\common\controller\Backend;
use think\Db;

/**
 * 数据统计管理
 *
 * @icon fa fa-user
 */
class DataStatistics extends Backend
{

    /**
     * @var \app\admin\model\NewUser
     */
    protected $model = null;

    public function _initialize()
    {
        parent::_initialize();
    }


    /**
     * 默认生成的控制器所继承的父类中有index/add/edit/del/multi五个基础方法、destroy/restore/recyclebin三个回收站方法
     * 因此在当前控制器中可不用编写增删改查的代码,除非需要自己控制这部分逻辑
     * 需要将application/admin/library/traits/Backend.php中对应的方法复制到当前控制器,然后进行修改
     */

    public function index(){



        $data = [];
        //节点相关
        $data['node']['smallNumber'] = self::nodeNumber(1);//小节点人数
        $data['node']['bigNumber'] = self::nodeNumber(2);//大节点人数
        $data['node']['superNumber'] = self::nodeNumber(3);//超级节点人数
        $data['node']['totalOrder'] = Db::table('buy_node_order')->where(['status'=>1])->sum('price');//总订单金额

        $allDataStatisticsUrl = self::allDataStatisticsUrl();
        //全网ART数据
        $data['art']['dayDestroyedNumber'] = $allDataStatisticsUrl['aTodayDestroy'];//今日ART销毁量
        $data['art']['circulationTotalNumber'] = $allDataStatisticsUrl['aCirculating'];//全网ART流通总量

        $data['art']['dynamicCalculated'] = $allDataStatisticsUrl['aDynamicPower'];//全网ART动态算力值
        $data['art']['staticCalculated'] = $allDataStatisticsUrl['aStaticPower'];//全网ART静态算力值:
        $data['art']['totalCalculated'] = $data['art']['dynamicCalculated'] + $data['art']['staticCalculated'];//全网ART算力值


        //全网SOUL数据
        $data['soul']['dayDestroyedNumber'] = $allDataStatisticsUrl['bTodayDestroy'];//今日SOUL销毁量
        $data['soul']['circulationTotalNumber'] = $allDataStatisticsUrl['bCirculating'];//全网SOUL流通总量

        $data['soul']['dynamicCalculated'] = $allDataStatisticsUrl['bDynamicPower'];//全网SOUL动态算力值
        $data['soul']['staticCalculated'] = $allDataStatisticsUrl['bStaticPower'];//全网SOUL静态算力值:
        $data['soul']['totalCalculated'] = $data['soul']['dynamicCalculated'] + $data['soul']['staticCalculated'];//全网SOUL算力值

        //令牌等级
        //团队等级数量
        $cardModelList = Db::table('card_model')
            ->alias('a')
            ->field('
                a.name,
                (select count(*) from mold_order b where a.id = b.card_model_id) as level_number
            ')
            ->order('a.id asc')
            ->select();
        $this->view->assign('cardModelList',$cardModelList);

        $data['model']['noOutputTotalNumber'] = round(Db::table('wallet_card')->sum('output'),2);//待产出总数量

        //初始化redis对象实例
        $redis = new \Redis();
        //连接redis服务器
        $redis_server = config('redis.sever') ;
        $redis_auth = config('redis.auth');
        $redis->connect($redis_server,config('redis.port'));
        $redis->auth($redis_auth);


        //用户数据
        $data['user']['artNoWithdrawn'] = round(self::userWithdrawInfo(1,0),2);//待提现
        $data['user']['artWithdrawnTotal'] = self::userWithdrawInfo(1,1);//ART提现总量
        $data['user']['artReleaseTotal'] = round(NewWallet::checkRedisValue($redis->get('send_token_a')),2);//ART释放总量


        $data['user']['soulNoWithdrawn'] = round(self::userWithdrawInfo(2,0),2);//待提现
        $data['user']['soulWithdrawnTotal'] = self::userWithdrawInfo(2,1);//SOUL提现总量
        $data['user']['soulReleaseTotal'] = round(NewWallet::checkRedisValue($redis->get('send_token_b')),2);//SOUL释放总量

        $this->view->assign('data', $data);

        return $this->view->fetch();
    }

    //统计节点数量
    public static function nodeNumber($node){
       return Db::table('wallet')->where(['node'=>$node])->count();
    }
    //统计节点等级
    public static function nodeLevelNumber($level){
        return Db::table('wallet')->where(['team_node_level'=>$level])->count();
    }
    //统计令牌数量
    public static function modelNumber($cardModelId){
       return Db::table('mold_order')->where(['status'=>1,'card_model_id'=>$cardModelId])->count();
    }
    //用户数据-提现
    public static function userWithdrawInfo($type,$status){
       return Db::table('check_in')->where(['type'=>$type,'status'=>$status])->sum('amount');
    }
    //掉接口获取客户端全网数据统计
    public static function allDataStatisticsUrl(){
        $arr["aTodayDestroy"] = 0;
        $arr["bTodayDestroy"] = 0;
        $arr["aCirculating"] = 0;
        $arr["bCirculating"] = 0;
        $arr["aStaticPower"] = 0;
        $arr["bStaticPower"] = 0;
        $arr["aDynamicPower"] = 0;
        $arr["bDynamicPower"] = 0;
        $url = config('allDataStatisticsUrl');
        $data = file_get_contents($url);
        $data = json_decode($data,true);
        if(is_array($data)){
            if($data['code'] == 200){
                $arr = $data['data'];
            }
        }
        return $arr;
    }
}
