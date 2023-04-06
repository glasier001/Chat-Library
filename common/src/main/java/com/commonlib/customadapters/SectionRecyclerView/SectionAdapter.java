package com.commonlib.customadapters.SectionRecyclerView;

import android.content.Context;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


//TODO 3/5/18 => Bhargav Savasani  => Note :- SectionAdapter use for Section RecyclerView
// Sectioned-RecyclerView
//use Resource Library below
//implementation 'com.github.IntruderShanky:Sectioned-RecyclerView:2.1.1'

public abstract class SectionAdapter<S extends SectionInterface<C>, C, SVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<S> sectionItemList;
    private int SECTION_VIEW_TYPE = 1;
    private int CHILD_VIEW_TYPE = 2;
    private int SHIMMER_VIEW_TYPE = 3;
    private boolean shimmerView;
    private List<SectionClass<S, C>> flatItemList;

    public SectionAdapter(Context context, List<S> sectionItemList) {
        this.sectionItemList = sectionItemList;
        this.flatItemList = this.generateFlatItemList(sectionItemList);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder childViewHolder;

        if (viewType == SHIMMER_VIEW_TYPE) {
            childViewHolder = this.onCreateShimmerViewHolder(viewGroup, viewType);
            return childViewHolder;
        } else if (this.isSectionViewType(viewType)) {
            childViewHolder = this.onCreateSectionViewHolder(viewGroup, viewType);
            return childViewHolder;
        } else {
            childViewHolder = this.onCreateChildViewHolder(viewGroup, viewType);
            return childViewHolder;
        }
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int flatPosition) {
//        if (flatPosition > this.flatItemList.size()) {
//            throw new IllegalStateException("Trying to bind item out of bounds, size " + this.flatItemList.size() + " flatPosition " + flatPosition + ". Was the data changed without a call to notify...()?");
//        }else

        if (shimmerView) {

        } else {
            SectionClass<S, C> sectionWrapper = (SectionClass) this.flatItemList.get(flatPosition);
            if (sectionWrapper.isSection()) {
                this.onBindSectionViewHolder(holder, sectionWrapper.getSectionPosition(), sectionWrapper.getSection());
            } else {
                this.onBindChildViewHolder(holder, sectionWrapper.getSectionPosition(), sectionWrapper.getChildPosition(), sectionWrapper.getChild());
            }

        }
    }

    public abstract RecyclerView.ViewHolder onCreateShimmerViewHolder(ViewGroup viewGroup, int viewType);

    public abstract SVH onCreateSectionViewHolder(ViewGroup viewGroup, int viewType);

    public abstract CVH onCreateChildViewHolder(ViewGroup viewGroup, int viewType);

    public abstract void onBindSectionViewHolder(RecyclerView.ViewHolder holder, int headerPostion, S var3);

    public abstract void onBindChildViewHolder(RecyclerView.ViewHolder holder, int headerPostion, int childPostion, C var4);


    public void startShimmer() {
        shimmerView = true;
        notifyDataSetChanged();
    }

    public void stopShimmer() {
        shimmerView = false;
    }

    private void generateSectionWrapper(List<SectionClass<S, C>> flatItemList, S section, int sectionPosition) {
        SectionClass<S, C> sectionWrapper = new SectionClass<>(section, sectionPosition);
        flatItemList.add(sectionWrapper);
        List<C> childList = section.getChildItems();

        for (int i = 0; i < childList.size(); ++i) {
            SectionClass<S, C> childWrapper = new SectionClass<>(childList.get(i), sectionPosition, i);
            flatItemList.add(childWrapper);
        }

    }

    private List<SectionClass<S, C>> generateFlatItemList(List<S> sectionItemList) {
        List<SectionClass<S, C>> flatItemList = new ArrayList();

        for (int i = 0; i < sectionItemList.size(); ++i) {
            S sectionInterface = (S) sectionItemList.get(i);
            this.generateSectionWrapper(flatItemList, sectionInterface, i);
        }

        return flatItemList;
    }


    public int getItemCount() {
        if (shimmerView) {
            return 20;
        } else {
            return this.flatItemList.size();
        }
    }

    public int getItemViewType(int position) {

        if (shimmerView) {
            return SHIMMER_VIEW_TYPE;
        } else {
            return ((SectionClass) this.flatItemList.get(position)).isSection() ? this.SECTION_VIEW_TYPE : this.CHILD_VIEW_TYPE;
        }
    }

    public boolean isSectionViewType(int viewType) {
        return viewType == this.SECTION_VIEW_TYPE;
    }

    public void insertNewSection(S section) {
        this.insertNewSection(section, this.sectionItemList.size());
    }

    public void insertNewSection(S section, int sectionPosition) {
        if (sectionPosition <= this.sectionItemList.size() && sectionPosition >= 0) {
            this.notifyDataChanged(this.sectionItemList);
        } else {
            throw new IndexOutOfBoundsException("sectionPosition =  " + sectionPosition + " , Size is " + this.sectionItemList.size());
        }
    }

    public void removeSection(int sectionPosition) {
        if (sectionPosition <= this.sectionItemList.size() - 1 && sectionPosition >= 0) {
            this.sectionItemList.remove(sectionPosition);
            this.notifyDataChanged(this.sectionItemList);
        } else {
            throw new IndexOutOfBoundsException("sectionPosition =  " + sectionPosition + " , Size is " + this.sectionItemList.size());
        }
    }

    public void insertNewChild(C child, int sectionPosition) {
        if (sectionPosition <= this.sectionItemList.size() - 1 && sectionPosition >= 0) {
            this.insertNewChild(child, sectionPosition, ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().size());
        } else {
            throw new IndexOutOfBoundsException("Invalid sectionPosition =  " + sectionPosition + " , Size is " + this.sectionItemList.size());
        }
    }

    public void insertNewChild(C child, int sectionPosition, int childPosition) {
        if (sectionPosition <= this.sectionItemList.size() - 1 && sectionPosition >= 0) {
            if (childPosition <= ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().size() && childPosition >= 0) {
                ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().add(childPosition, child);
                this.notifyDataChanged(this.sectionItemList);
            } else {
                throw new IndexOutOfBoundsException("Invalid childPosition =  " + childPosition + " , Size is " + ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().size());
            }
        } else {
            throw new IndexOutOfBoundsException("Invalid sectionPosition =  " + sectionPosition + " , Size is " + this.sectionItemList.size());
        }
    }

    public void removeChild(int sectionPosition, int childPosition) {
        if (sectionPosition <= this.sectionItemList.size() - 1 && sectionPosition >= 0) {
            if (childPosition <= ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().size() - 1 && childPosition >= 0) {
                ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().remove(childPosition);
                this.notifyDataChanged(this.sectionItemList);
            } else {
                throw new IndexOutOfBoundsException("Invalid childPosition =  " + childPosition + " , Size is " + ((SectionInterface) this.sectionItemList.get(sectionPosition)).getChildItems().size());
            }
        } else {
            throw new IndexOutOfBoundsException("Invalid sectionPosition =  " + sectionPosition + " , Size is " + this.sectionItemList.size());
        }
    }

    public void notifyDataChanged(List<S> sectionItemList) {
        this.flatItemList = new ArrayList();
        this.flatItemList = this.generateFlatItemList(sectionItemList);
        this.notifyDataSetChanged();
    }

}
