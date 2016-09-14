# GridLayoutExample
一个类似于gridview的view控件，可以设置行数和列数，然后按照行或者列进行排列。
使用方法：
在value文件下新建attrs文件，添加如下属性：
<declare-styleable name="GridLayoutAttrs">
        <attr name="horizontalSpacing" format="dimension" />
        <attr name="verticalSpacing" format="dimension" />
        <attr name="numColumns" format="integer" />
        <attr name="numRows" format="integer" />
</declare-styleable>

在你所需要的xml中，添加控件：
 <loner.library.gridlayout.GridLayout
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="@color/colorAccent"
        android:paddingTop="20dp"
        app:horizontalSpacing="10dp"
        app:numRows="4"
        app:verticalSpacing="10dp">
</loner.library.gridlayout.GridLayout>
在这个控件下面可以直接添加view，或者java中手动添加view，自定义的gridlayout会根据你设置的numRows或者numColumns
来对添加的view进行行或者排序，如果按照行来排列，垂直方向会平均分配高度，如果按照列来进行排列，水平方向上平均
分配宽度。horizontalSpacing和verticalSpacing分别代表水平方向和垂直方向的间隔。
具体可以看一下这个example。
