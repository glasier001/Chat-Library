package com.commonlib.customadapters.SectionRecyclerView;

import java.util.List;

//TODO  3/5/18 => Bhargav Savasani  => Note :- SectionInterface implement your class to use in Section RecyclerView

public interface SectionInterface<C> {
    List<C> getChildItems();
}
