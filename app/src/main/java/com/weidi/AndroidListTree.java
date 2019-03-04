package com.weidi;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AndroidListTree {

    private static final String TAG =
            AndroidListTree.class.getSimpleName();

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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("/");
        stringBuilder.append(rootNode.mTitle);
        rootNode.mPath = stringBuilder.toString();

        if (!isAddedWithAllNodes(rootNode)) {
            mAllNodes.add(rootNode);
        }
        if (!isAddedWithRootNodes(rootNode)) {
            mRootNodes.add(rootNode);
        }
        if (!isAddedWithShowedNodes(rootNode)) {
            mShowedNodes.add(rootNode);
        }
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
    }

    public void addChildNode(ItemNode parentNode, ItemNode childNode) {
        if (parentNode == null || childNode == null) {
            return;
        }

        childNode.mParentNode = parentNode;

        StringBuilder stringBuilder = new StringBuilder();
        if (childNode.mParentNode != null) {
            stringBuilder.append(childNode.mParentNode.mPath);
        }
        stringBuilder.append("/");
        stringBuilder.append(childNode.mTitle);
        childNode.mPath = stringBuilder.toString();

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
    }

    public void removeChildNode(ItemNode parentNode, ItemNode childNode) {
        if (parentNode == null || childNode == null) {
            return;
        }
        if (isAddedWithAllNodes(childNode)) {
            mAllNodes.remove(childNode);
        }
        removeChildToParent(parentNode, childNode);
    }

    /*public ItemNode getFocusedNode() {

    }*/

    public int getFocusedIndex(ItemNode node) {
        int index = -1;
        if (node == null) {
            return index;
        }
        int size = mShowedNodes.size();
        for (int i = 0; i < size; i++) {
            ItemNode node1 = mShowedNodes.get(i);
            if (node.mPath.equals(node1.mPath)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public ItemNode getNodeWithPosition(int position) {
        if (position < 0 || position >= mShowedNodes.size()) {
            return null;
        }
        return mShowedNodes.get(position);
    }

    public void handleSort() {
        List<ItemNode> newShowedNodes = new ArrayList<>();
        for (ItemNode node : mRootNodes) {
            newShowedNodes.add(node);
            test1(newShowedNodes, node);
        }
        mShowedNodes.clear();
        for (ItemNode node : newShowedNodes) {
            mShowedNodes.add(node);
        }
        printNodes();
    }

    public void handleExpandNode(ItemNode node) {
        if (node == null) {
            return;
        }
        node.mNeedToShow = true;
        node.mIsExpanded = true;
        handleExpandNodeImpl(node);
    }

    public void handleCollapseNode(ItemNode node) {
        if (node == null) {
            return;
        }
        node.mNeedToShow = true;
        node.mIsExpanded = false;
        handleCollapseNodeImpl(node);
    }

    private void handleExpandNodeImpl(ItemNode node) {
        for (ItemNode node1 : node.mChildNodes) {
            node1.mNeedToShow = true;
            if (node1.mIsExpanded) {
                handleExpandNodeImpl(node1);
            }
        }
    }

    private void handleCollapseNodeImpl(ItemNode node) {
        for (ItemNode node1 : node.mChildNodes) {
            if (node1.mNeedToShow) {
                node1.mNeedToShow = false;
                handleCollapseNodeImpl(node1);
            }
        }
    }

    private void printNodes() {
        boolean needToPrint = true;
        for (ItemNode node : mShowedNodes) {
            if (needToPrint)
                Log.i(TAG, "node.mPath: " + node.mPath);
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
