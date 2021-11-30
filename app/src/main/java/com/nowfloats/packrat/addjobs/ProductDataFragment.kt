package com.nowfloats.packrat.addjobs

/*
class ProductDataFragment : Fragment(), ProdClickListener {
    private lateinit var dataModel: AddProductViewModel
    private lateinit var prodAdapter: ProductDataAdapter
    private lateinit var bottomViewDialog: FullBottomSheetDialogFragment
    private var isclickBottomView = false
    //private var allProducts: ArrayList<Int>? = ArrayList<Int>()
    //private var productList:ArrayList<ArrayList<metaDataBeanItem>> = ArrayList<ArrayList<metaDataBeanItem>>()

//    private var alllistProduct: ArrayList<ArrayList<Int>?>? = ArrayList<ArrayList<Int>?>()

    //    val linkList: LinkedList<ArrayList<Int>> = LinkedList(alllistProduct)
    lateinit var ctx: Context


    //    private var prodList = emptyList<ProductEntityClass>()
    private var addclick_position = 0
    private var tabPosition = 0
    private var _hasLoadedOnce = false
    lateinit var viewObj:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataModel = ViewModelProviders.of(requireActivity()).get(AddProductViewModel::class.java)
        try {
            val items = arguments?.getParcelable<metaDataBeanItem>(AppConstant.REQUEST_TYPE)
            val item = arguments?.getParcelable<metaDataBeanItem>(AppConstant.REQUEST_TYPE) as ArrayList<metaDataBeanItem>
            //productList = items as ArrayList<metaDataBeanItem>

        }catch (e:Exception){
            e.printStackTrace()
        }
        if (getArguments() != null) {
            *//*if (allProducts!!.size > 0)
                allProducts!!.clear()
            allProducts = getArguments()?.getIntegerArrayList(ARG_PRODUCTS);
            alllistProduct!!.add(allProducts)*//*
            tabPosition = getArguments()!!.getInt("KEY_POSITION")
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && _hasLoadedOnce) {
            *//* val ftr: FragmentTransaction = fragmentManager!!.beginTransaction()
             ftr.detach(this).attach(this).commit()
             prodAdapter.notifyDataSetChanged()
             _hasLoadedOnce = true*//*
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.product_item, container, false)
        viewObj = view
        setRecyclerView(productList)
        setObserver()
        return view
    }

    override fun onPause() {
        super.onPause()
        //will update after test
    }

    fun setObserver() {
        dataModel.clickadd.observe(this, Observer {
            val viewHolder: ProductDataAdapter.PickerViewHolder? = add_recyclerview.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
            prodAdapter.updateList(ArrayList<metaDataBeanItem>(), viewHolder)
        })
        dataModel.addBottomClick.observe(this, Observer {
            if (isclickBottomView) {
                val viewHolder: ProductDataAdapter.PickerViewHolder? = add_recyclerview.findViewHolderForAdapterPosition(addclick_position) as ProductDataAdapter.PickerViewHolder?
                it!!?.let { it1 -> prodAdapter.setFormView(it1, viewHolder!!, addclick_position) }
                isclickBottomView = false
            }

        })
        dataModel.clickdeleteview.observe(this, Observer {
            prodAdapter.deleteview(it)
        })

        //setRecyclerView(productList)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        text_view.text = tabPosition.toString() + "  " + allProducts!!.size+"  "+prodAdapter.viewList!!.size
//        setRecyclerView()
    }


    private fun setRecyclerView( prducts :ArrayList<ArrayList<metaDataBeanItem>>) {
        try {
            val linearLayoutManager =
                LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
            linearLayoutManager.setReverseLayout(true)
            viewObj.add_recyclerview.layoutManager = linearLayoutManager
            prodAdapter = ProductDataAdapter(ctx, this, productList)
            viewObj.add_recyclerview.adapter = prodAdapter
            prodAdapter.setData(prducts!!)
            prodAdapter.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
//        prodAdapter.notifyItemChanged(tabPosition)
//        prodAdapter.setData(allProducts!!)
        *//* add_recyclerview.apply {
             layoutManager = linearLayoutManager
             adapter = prodAdapter
             prodAdapter.setData(allProducts!!)
         }*//*
    }


    companion object {
        private val ARG_PRODUCTS = "PRODS"

        fun newInstance(
            ctx: Context,
            tabPosition: Int,
            pList: ArrayList<metaDataBeanItem>
        ): ProductDataFragment {
            *//*  val args = Bundle()
              args.putInt(Positionval, tabPosition)
              args.putInt(Positionval, pList)
              val fragment = ProductDataFragment()
              fragment.arguments = args
              return fragment*//*
            val fragment = ProductDataFragment()
            fragment.ctx = ctx
            val args = Bundle()
            args.putParcelable(AppConstant.REQUEST_TYPE, pList as ArrayList<metaDataBeanItem>)
            args.putInt("KEY_POSITION", tabPosition)
            fragment.arguments = args
            return fragment
            *//*val tabFragment = ProductDataFragment()
            tabFragment.tabPosition = tabPosition
            return tabFragment*//*
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClickAdd(position: Int) {
//        bottomViewDialog = BottomViewAddProductData()
       *//* println("values>>>0>$position")
        addclick_position = position
        isclickBottomView = true
        bottomViewDialog = FullBottomSheetDialogFragment(position)
        bottomViewDialog.setStyle(0, R.style.BottomSheetDialog)
        bottomViewDialog.show(fragmentManager!!, BottomViewDialog.TAG)*//*
    }


    override fun onClickItemDelete(position: Int?) {
        dataModel.deleteViewOnClick(position!!)
    }

    override fun onItemDeleteAtPos(position: Int, productList: ArrayList<metaDataBeanItem>) {
     }

    *//*fun setTabclickRefresh(tabPosition: Int) {
        try {
            var selectedTabProducts = dataModel.fragmentMapObj.get(tabPosition)
            if (selectedTabProducts != null) {
                prodAdapter = ProductDataAdapter(ctx, this, selectedTabProducts)
                android.os.Handler().postDelayed({
                    GlobalScope.launch(Dispatchers.Main) {
                        setRecyclerView(selectedTabProducts)
                    }
                }, 500)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }*//*
        *//*if (SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi") != null)
            println("chaeck_tab_click>0>  $tabPosi  =  $tabPosition  ${SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.size}")
        if (SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi") != null) {
            if (productList!!.size>0){
            }
                //productList!!.clear()
            for (i in 0 until SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.size) {
                //productList!!.add(SharedPreferencesManager(ctx).getListFromLocal("item_$tabPosi")!!.get(i)!!)
            }
        }else{
            println("chaeck_tab_click>0>else  $tabPosi  =  $tabPosition  ${productList!!.size}")
        }
        android.os.Handler().postDelayed({
            GlobalScope.launch(Dispatchers.Main) {
                setRecyclerView()
            }
        }, 500)*//*
    }

    fun setTabclickUnselect(tabPosition: Int) {
            //dataModel.updateFragmentIndex(tabPosition, prodAdapter.parentProductList)
    }

}*/
