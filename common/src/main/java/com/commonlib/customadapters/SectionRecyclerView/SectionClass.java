package com.commonlib.customadapters.SectionRecyclerView;

//TODO  3/5/18 => Bhargav Savasani  => Note :- SectionClass use for in SectionAdapter

public class SectionClass<S extends SectionInterface<C>, C> {

    private boolean sectionItem;
    private S section;
    private C child;
    private int sectionPosition;
    private int childPosition;

    public SectionClass(S section, int sectionPosition) {
        this.sectionItem = true;
        this.section = section;
        this.sectionPosition = sectionPosition;
        this.childPosition = -1;
    }

    public SectionClass(C child, int sectionPosition, int childPosition) {
        this.child = child;
        this.sectionPosition = sectionPosition;
        this.sectionItem = false;
        this.childPosition = childPosition;
    }
    public boolean isSection() {
        return this.sectionItem;
    }

    public S getSection() {
        return this.section;
    }

    public C getChild() {
        return this.child;
    }

    public int getSectionPosition() {
        return this.sectionPosition;
    }

    public int getChildPosition() {
        if (this.childPosition == -1) {
            throw new IllegalAccessError("This is not child");
        } else {
            return this.childPosition;
        }
    }
}
