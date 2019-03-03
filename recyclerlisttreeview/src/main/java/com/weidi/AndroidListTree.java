package com.weidi;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AndroidListTree {

    private static final String TAG =
            AndroidListTree.class.getSimpleName();

    /**
     * 用于遍历，代表遍历的位置
     * 注意，在树遍历过程中切不可改变树结构！！！！！！！！！！！！！！
     */
    public class EnumPos {
        private class TreeEnumInfo {
            //当前的Node List，因为收起的Node的儿子或儿孙也是位于一个List中的
            private List<ItemNode> nodeList;
            //当前nodeList上的第几个
            private int planeIndex;

            public TreeEnumInfo(List<ItemNode> nodeList, int planeIndex) {
                this.nodeList = nodeList;
                this.planeIndex = planeIndex;
            }
        }

        private Stack<TreeEnumInfo> treeEnumStack = new Stack<>();

        private EnumPos() {
            treeEnumStack.push(new TreeEnumInfo(mAllNodes, 0));
        }
    }

    private int rootNodesCount = 0;

    //用List保存整棵树
    private List<ItemNode> mAllNodes = new ArrayList<>();
    private List<ItemNode> mRootNodes = new ArrayList<>();
    private List<ItemNode> mShowedNodes = new ArrayList<>();

    public List<ItemNode> getAllNodes() {
        return mAllNodes;
    }

    public List<ItemNode> getRootNodes() {
        return mRootNodes;
    }

    public List<ItemNode> getShowedNodes() {
        return mShowedNodes;
    }

    public void addRootNode(ItemNode rootNode) {
        if (rootNode == null) {
            return;
        }
        rootNode.mParentNode = null;
        rootNode.mNeedToShow = true;
        rootNode.mHierarchy = 0;
        if (!isAddedWithAllNodes(rootNode)) {
            mAllNodes.add(rootNode);
        }
        if (!isAddedWithRootNodes(rootNode)) {
            mRootNodes.add(rootNode);
        }
        if (!isAddedWithShowedNodes(rootNode)) {
            mShowedNodes.add(rootNode);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/");
        stringBuilder.append(rootNode.mTitle);
        rootNode.mPath = stringBuilder.toString();

        handleSort();
    }

    public void removeRootNode(ItemNode rootNode) {
        if (rootNode == null) {
            return;
        }
        rootNode.mParentNode = null;
        if (isAddedWithAllNodes(rootNode)) {
            mAllNodes.remove(rootNode);
        }
        if (isAddedWithRootNodes(rootNode)) {
            mRootNodes.remove(rootNode);
        }
        if (isAddedWithShowedNodes(rootNode)) {
            mShowedNodes.remove(rootNode);
        }
        handleSort();
    }

    public void addChildNode(ItemNode parentNode, ItemNode childNode) {
        if (parentNode == null || childNode == null) {
            return;
        }
        if (!isAddedWithAllNodes(parentNode)) {
            mAllNodes.add(parentNode);
        }
        if (!isAddedWithAllNodes(childNode)) {
            mAllNodes.add(childNode);
        }
        if (parentNode.mNeedToShow) {
            if (!isAddedWithShowedNodes(parentNode)) {
                mShowedNodes.add(parentNode);
            }
        }
        if (childNode.mNeedToShow) {
            if (!isAddedWithShowedNodes(childNode)) {
                mShowedNodes.add(childNode);
            }
        }
        childNode.mParentNode = parentNode;

        StringBuilder stringBuilder = new StringBuilder();
        if (childNode.mParentNode != null) {
            stringBuilder.append(childNode.mParentNode.mPath);
        }
        stringBuilder.append("/");
        stringBuilder.append(childNode.mTitle);
        childNode.mPath = stringBuilder.toString();

        addChildToParent(parentNode, childNode);
        int hierarchy = 0;
        ItemNode pNode = childNode.mParentNode;
        while (true) {
            if (pNode == null) {
                break;
            }
            ++hierarchy;
            pNode = pNode.mParentNode;
        }
        childNode.mHierarchy = hierarchy;
        handleSort();
        printNodes();
    }

    public void removeChildNode(ItemNode parentNode, ItemNode childNode) {
        if (parentNode == null || childNode == null) {
            return;
        }
        if (isAddedWithAllNodes(childNode)) {
            mAllNodes.remove(childNode);
        }
        removeChildToParent(parentNode, childNode);
        handleSort();
    }

    /*public ItemNode getFocusedNode() {

    }*/

    public ItemNode getNodeWithPosition(int position) {
        if (position < 0 || position >= mShowedNodes.size()) {
            return null;
        }
        return mShowedNodes.get(position);
    }

    private void printNodes() {
        for (ItemNode node : mShowedNodes) {
            Log.i(TAG, "node.mPath: " + node.mPath);
        }
    }

    private void handleSort() {
        List<ItemNode> newShowedNodes = new ArrayList<>();
        for (ItemNode node : mRootNodes) {
            newShowedNodes.add(node);
            test1(newShowedNodes, node);
        }
        mShowedNodes.clear();
        for (ItemNode node : newShowedNodes) {
            mShowedNodes.add(node);
        }
    }

    private void test1(List<ItemNode> newShowedNodes, ItemNode node) {
        List<ItemNode> childNodes = node.mChildNodes;
        if (childNodes.isEmpty()) {
            return;
        }
        for (ItemNode node1 : childNodes) {
            if (node1.mNeedToShow) {
                newShowedNodes.add(node1);
                test1(newShowedNodes, node1);
            }
        }
    }

    private void addChildToParent(ItemNode parentNode, ItemNode childNode) {
        List<ItemNode> childNodes = parentNode.mChildNodes;
        boolean isAdded = false;
        for (ItemNode node : childNodes) {
            if (node.mPath.equals(childNode.mPath)) {
                isAdded = true;
                break;
            }
        }
        if (!isAdded) {
            childNodes.add(childNode);
        }
    }

    private void removeChildToParent(ItemNode parentNode, ItemNode childNode) {
        List<ItemNode> childNodes = parentNode.mChildNodes;
        ItemNode tempNode = null;
        for (ItemNode node : childNodes) {
            if (node.mPath.equals(childNode.mPath)) {
                tempNode = node;
                break;
            }
        }
        if (tempNode != null) {
            childNodes.remove(childNode);
        }
    }

    private boolean isAddedWithAllNodes(ItemNode node) {
        boolean isAdded = false;
        if (node == null) {
            return isAdded;
        }
        for (ItemNode node_ : mAllNodes) {
            if (node.mPath.equals(node_.mPath)) {
                isAdded = true;
                break;
            }
        }
        return isAdded;
    }

    private boolean isAddedWithRootNodes(ItemNode node) {
        boolean isAdded = false;
        if (node == null) {
            return isAdded;
        }
        for (ItemNode node_ : mRootNodes) {
            if (node.mPath.equals(node_.mPath)) {
                isAdded = true;
                break;
            }
        }
        return isAdded;
    }

    private boolean isAddedWithShowedNodes(ItemNode node) {
        boolean isAdded = false;
        if (node == null) {
            return isAdded;
        }
        for (ItemNode node_ : mShowedNodes) {
            if (node.mPath.equals(node_.mPath)) {
                isAdded = true;
                break;
            }
        }
        return isAdded;
    }

}
