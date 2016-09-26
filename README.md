# 简介
一个类似于gridview的view控件，可以设置行数和列数，然后按照行或者列进行排列。

# 使用介绍
在value文件下新建attrs文件，添加如下属性：
``` java
<declare-styleable name="GridLayoutAttrs">
        <attr name="horizontalSpacing" format="dimension" />
        <attr name="verticalSpacing" format="dimension" />
        <attr name="numColumns" format="integer" />
        <attr name="numRows" format="integer" />
</declare-styleable>
```

在你所需要的xml中，添加控件：
``` java
 <loner.library.gridlayout.GridLayout
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:background="@color/colorAccent"
        android:paddingTop="20dp"
        app:horizontalSpacing="10dp"
        app:numRows="4"
        app:verticalSpacing="10dp">
</loner.library.gridlayout.GridLayout>
```

# 注意
-  numColumns和numRows可以都声明，但是你的view数目要>=numColumns*numRows，否则会抛异常。这里布局跟gridview布局一样，以行为来排列。 
-  你也可以只声明numColumns和numRows其中一个，但是如果只声明其中一个的话，这里布局就会跟着列或者行来进行布局。如果我说的比较抽象，大家可以用github上的例子去试一下。
