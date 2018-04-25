# PopupLayout
this lib is a  popuplayout 

# Effect

![image](https://github.com/dalong982242260/PopupLayout/blob/master/gif/popup1.gif?raw=true)

# How to use


    <com.dl.popuplayout.PopupLayout
        android:id="@+id/buttomPopup"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        app:animDuration="300"
        app:animType="anim_right"
        app:canceledOnTouchOutside="true"
        app:isAnim="true">

      <!-- Any layout，must one chind-->

    </com.dl.popuplayout.PopupLayout>