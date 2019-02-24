package com.niuedu;

import android.util.Pair;
import android.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by nkm on 27/12/2017.
 * <p>
 * 原则：儿子一定是在爸爸后面并紧靠爸爸的
 * <p>
 * 以List的形式表式树。用起来感觉是树，但实际上是个List。这带来很多好处：
 * 1 首先就是没有递归算法。该用递归的地方全部变成了循环。
 * 2 其次是有序，插入节点时，可以指定它是它爸爸的第几个儿子。
 * 3 最后就是极其适合在RecyclerView中使用。使用此类做后台数据，
 * 跟List无异，无论根节点行还是子节点行都是RecyclerView中的一行。
 * 4 不需对RecyclerView做任何改动
 * <p>
 * 有诗为证：
 * 远看像棵树
 * 近看不是树
 * 似树而非树
 * 是为牛逼树
 */
public class ListTree {
    public class TreeNode {
        //实际的数据
        private Object data;

        //本node所使用的layout id
        private int layoutResId;

        //儿子们的数量，儿子们一定是紧挨着爸爸放置的
        private int childrenCount = 0;

        //其所有子孙们的数量，这个在收起必须为0,在展开时才有效
        private int descendantCount = 0;

        private TreeNode parent = null;

        private boolean checked;

        //是否显示展开－收起图标
        private boolean showExpandIcon = true;

        //当此节点折叠时，其子孙们不能再位于List中，所以移到这里来保存。
        //当此节点展开时，再移到List中
        private List<TreeNode> collapseDescendant;
        private boolean expand;
        private int layerLevel;

        private TreeNode(TreeNode parent, Object data, int layoutResId) {
            this.parent = parent;
            this.data = data;
            this.layoutResId = layoutResId;
        }

        //把子孙们收回
        public void retractDescendant(List<TreeNode> collapseChildren) {
            //Must clone a array
            this.collapseDescendant = new ArrayList<>();
            for (TreeNode node : collapseChildren) {
                this.collapseDescendant.add(node);
            }
        }

        //把子孙们抽走
        public List<TreeNode> extractDescendant() {
            List<TreeNode> ret = this.collapseDescendant;
            this.collapseDescendant = null;
            return ret;
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setExpand(boolean expand) {
            this.expand = expand;
        }

        public Object getData() {
            return data;
        }

        public int getLayoutResId() {
            return layoutResId;
        }

        public boolean isExpand() {
            return expand;
        }

        public boolean isChecked() {
            return checked;
        }

        public boolean isShowExpandIcon() {
            return showExpandIcon;
        }

        public void setShowExpandIcon(boolean b) {
            showExpandIcon = b;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public void setDescendantChecked(boolean b) {
            if(this.collapseDescendant==null){
                return;
            }

            if (this.expand) {
                throw new IllegalStateException("Only can invoke when node is collapsed");
            }

            for (TreeNode node : this.collapseDescendant) {
                node.setChecked(b);
            }
        }

        public int getChildrenCount() {
            return childrenCount;
        }

        public int getDescendantCount() {
            return descendantCount;
        }
    }

    /**
     * 用于遍历，代表遍历的位置
     * 注意，在树遍历过程中切不可改变树结构！！！！！！！！！！！！！！
     */
    public class EnumPos {
        private class TreeEnumInfo {
            //当前的Node List，因为收起的Node的儿子或儿孙也是位于一个List中的
            private List<TreeNode> nodeList;
            //当前nodeList上的第几个
            private int planeIndex;

            public TreeEnumInfo(List<TreeNode> nodeList, int planeIndex) {
                this.nodeList = nodeList;
                this.planeIndex = planeIndex;
            }
        }

        private Stack<TreeEnumInfo> treeEnumStack = new Stack<>();

        private EnumPos() {
            treeEnumStack.push(new TreeEnumInfo(nodes, 0));
        }
    }

    private int rootNodesCount;

    //用List保存整棵树
    private List<TreeNode> nodes = new ArrayList<>();


    /**
     * @param parent      其父node，若添加root node，则父node传null即可
     * @param data        node中所包含的用户数据
     * @param layoutResId node的行layout资源id
     * @return 刚添加的node
     */
    public TreeNode addNode(TreeNode parent, Object data, int layoutResId) {
        TreeNode node = new TreeNode(parent, data, layoutResId);
        if (parent == null) {
            //root node,append to end
            nodes.add(node);
            rootNodesCount++;
            return node;
        }

        //插入非root node，有爹
        if (parent.isExpand()) {
            //如果parent当前状态是展开的
            int index = nodes.indexOf(parent);
            index += parent.descendantCount;

            //插到最后一个子孙的后面
            nodes.add(index + 1, node);
            //需追溯它所有的长辈，为每个都更新其子孙数量
            TreeNode ancestor = parent;
            while (ancestor != null) {
                ancestor.descendantCount++;
                ancestor = ancestor.parent;
            }
        } else {
            //如果parent当前状态是收起的
            if (parent.collapseDescendant == null) {
                parent.collapseDescendant = new ArrayList<>();
            }
            parent.collapseDescendant.add(node);
        }

        //add children count
        parent.childrenCount++;

        return node;
    }

//    /**
//     * Get a List witch conatins all items;
//     * @return List
//     */
//    public List<TreeNode> getNodeList(){
//        //如果有节点处于收起状态，底层是List中就不能包含所有的Item
//        //
//        return this.nodes;
//    }

    /**
     * 开始遍历
     * 如果返回不为null，则可以继续调用getNextNode()进行遍历。
     * 遍历顺序并不一定符合树的遍历顺序。
     *
     * @return 根上第一个node的位置，如果为null，则不能继续调用getNextNode()
     */
    public EnumPos startEnumNode() {
        if (nodes.isEmpty()) {
            return null;
        }

        return new EnumPos();
    }

    /**
     * 用于遍历，获取下一个节点
     * 注意！！返回的与参数其实是一个对象！
     * @param pos 当前节点的位置，既是输入参数也是输出参数
     * @return 返回的其实是改变了内部属性的参数pos，当返回为null时，需停止遍历
     */
    public EnumPos enumNext(EnumPos pos) {
        EnumPos.TreeEnumInfo info = pos.treeEnumStack.peek();
        TreeNode curNode = info.nodeList.get(info.planeIndex);
        if (curNode.getChildrenCount() > 0 && !curNode.isExpand()) {
            //如果这个Node没展开且有儿子，则需要遍历它的儿子
            pos.treeEnumStack.push(pos.new TreeEnumInfo(curNode.collapseDescendant, 0));
            return pos;
        }
        //如果有儿子且展开了，planeIndex+1，或者没有孩子
        while (!pos.treeEnumStack.empty()) {
            pos.treeEnumStack.peek().planeIndex++;
            if (pos.treeEnumStack.peek().planeIndex == pos.treeEnumStack.peek().nodeList.size()) {
                //如果是当前List的最后一个了，则弹出
                pos.treeEnumStack.pop();
            } else {
                return pos;
            }
        }
        return null;
    }

    /**
     * 获取当前遍历到的节点
     *
     * @param pos 节点序号
     * @return 节点对象，必不为null
     */
    public TreeNode getNodeByEnumPos(EnumPos pos) {
        TreeNode node = pos.treeEnumStack.peek().nodeList.get(pos.treeEnumStack.peek().planeIndex);
        return node;
    }

    /**
     * @param parent      爸爸
     * @param position    属于爸爸的第几子
     * @param data        包含的用户数据
     * @param layoutResId layout资原id
     * @return 刚添加的node
     */
    public TreeNode insertNode(TreeNode parent, int position, Object data, int layoutResId) {
        TreeNode node = new TreeNode(parent, data, layoutResId);

        if (parent == null) {
            //insert root node
            if (position > rootNodesCount) {
                //插入位置超出范围
                return null;
            }

            nodes.add(position, node);
            rootNodesCount++;
            return node;
        }

        //插入非root node，有爹
        if (position > parent.childrenCount) {
            //插入位置超出范围
            return null;
        }

        if (parent.isExpand()) {
            int planePosition = getNodePlaneIndexByIndex(parent, position);
            nodes.add(planePosition, node);

            //需追溯它所有的长辈，为每个都更新其子孙数量
            TreeNode ancestor = parent;
            while (ancestor != null) {
                ancestor.descendantCount++;
                ancestor = ancestor.parent;
            }
        } else {
            if (parent.collapseDescendant == null) {
                parent.collapseDescendant = new ArrayList<>();
            }
            parent.collapseDescendant.add(position, node);
        }
        //add children count
        parent.childrenCount++;
        return node;
    }

    /**
     * @param parent if null , return root node
     * @param index  它是从树的角度来讲的序号
     * @return plane index,<0 means exceed range
     */
    private int getNodePlaneIndexByIndex(TreeNode parent, int index) {
        if (!parent.isExpand()) {
            throw new IllegalStateException("Only invoke when parent is expand");
        }

        int range = 0;
        if (parent == null) {
            //get index of root node
            range = this.rootNodesCount;
        } else {
            range = parent.childrenCount;
        }

        int planeIndex = 0;

        for (int i = 0; i < range; i++) {
            if (i == index) {
                return planeIndex;
            } else {
                //指向下一个root node
                TreeNode node = nodes.get(planeIndex);
                planeIndex += node.isExpand() ? node.descendantCount : 0;
                planeIndex++;
            }
        }

        return -1;
    }

    //返回被删除了Item的start plane index和count
    public Pair<Integer,Integer> clearDescendant(TreeNode treeNode) {
        if (treeNode.childrenCount == 0) {
            //如果没有儿子，无法收起
            return null;
        }

        //如果有儿子，把子孙们从List中取出来
        int nodePlaneIndex = nodes.indexOf(treeNode);
        Pair<Integer,Integer> ret = new Pair<Integer,Integer>(
                nodePlaneIndex+1, treeNode.descendantCount);
        List<TreeNode> descendant = nodes.subList(
                nodePlaneIndex + 1,
                nodePlaneIndex + 1 + treeNode.descendantCount);
        descendant.clear();

        treeNode.childrenCount=0;
        treeNode.descendantCount=0;
        treeNode.collapseDescendant=null;

        return ret;
    }

    /**
     * remove node and its descendant
     *
     * @param node node to be removed
     */
    public void removeNode(TreeNode node) {
        TreeNode parent = node.parent;
        if (parent==null || parent.isExpand()) {
            int descendantCount = node.descendantCount;
            int index = nodes.indexOf(node);
            List ret = nodes.subList(index, index + descendantCount + 1);
            ret.clear();//remove nodes and its descendant

            if (parent != null) {
                parent.childrenCount++;

                //需追溯它所有的长辈，为每个都更新其子孙数量
                TreeNode ancestor = parent;
                while (ancestor != null) {
                    ancestor.descendantCount -= descendantCount;
                    ancestor = ancestor.parent;
                }
            } else {
                rootNodesCount--;
            }
        } else {
            if (parent == null) {
                rootNodesCount--;
            } else {
                parent.childrenCount++;
                parent.descendantCount--;
            }
            nodes.remove(node);
        }
    }

    //获取节点在列表中的索引
    public TreeNode getNodeByPlaneIndex(int index) {
        return nodes.get(index);
    }

    public int getNodePlaneIndex(TreeNode node) {
        return nodes.indexOf(node);
    }

    public int expandNode(TreeNode node) {
        int nodePlaneIndex=nodes.indexOf(node);

        if (node.isExpand()) {
            throw new IllegalStateException("Only invoke when parent is collesped");
        }

        node.setExpand(true);
        if (node.childrenCount == 0) {
            //如果没有儿子，无法展开
            return 0;
        }

        //如果有儿子，把儿子们移到List中
        List<TreeNode> descendant = node.extractDescendant();
        nodes.addAll(nodePlaneIndex + 1, descendant);
        node.descendantCount = descendant.size();

        //需追溯它所有的长辈，为每个都更新其子孙数量
        TreeNode ancestor = node.parent;
        while (ancestor != null) {
            ancestor.descendantCount += node.descendantCount;
            ancestor = ancestor.parent;
        }

        return node.descendantCount;
    }

    public int expandNode(int nodePlaneIndex) {
        TreeNode node = nodes.get(nodePlaneIndex);
        return expandNode(node);
    }

    //返回影响到的Node们的数量
    public int collapseNode(TreeNode node) {
        int nodePlaneIndex = nodes.indexOf(node);
        if (!node.isExpand()) {
            throw new IllegalStateException("Only invoke when parent is expand");
        }
        node.setExpand(false);

        if (node.childrenCount == 0) {
            //如果没有儿子，无法收起
            return 0;
        }

        //如果有儿子，把子孙们从List中取出自己保存
        List<TreeNode> descendant = nodes.subList(
                nodePlaneIndex + 1, nodePlaneIndex + 1 + node.descendantCount);

        node.retractDescendant((List<TreeNode>) descendant);
        //在List中删掉这一段
        descendant.clear();

        //需追溯它所有的长辈，为每个都更新其子孙数量
        TreeNode ancestor = node.parent;
        while (ancestor != null) {
            ancestor.descendantCount -= node.descendantCount;
            ancestor = ancestor.parent;
        }

        return node.descendantCount;
    }
    public int collapseNode(int nodePlaneIndex) {
        TreeNode node = nodes.get(nodePlaneIndex);
        return collapseNode(node);
    }

    public int setDescendantChecked(int nodePlaneIndex, boolean b) {
        TreeNode node = nodes.get(nodePlaneIndex);
        if (node.isExpand()) {
            int start = nodePlaneIndex + 1;
            int count = node.descendantCount;
            for (int i = start; i < start + count; i++) {
                nodes.get(i).setChecked(b);
            }
            return node.descendantCount;
        } else {
            node.setDescendantChecked(b);
            return 0;
        }
    }

    public int size() {
        return nodes.size();
    }

    public int getNodeLayerLevel(TreeNode node) {
        int count = 0;
        TreeNode parent = node.parent;
        while (parent != null) {
            count++;
            parent = parent.parent;
        }
        return count;
    }

    public void removeCheckedNodes() {
        //从前往后遍历，先找到的是父辈，如果父辈被删，其子孙必然被删掉
        //所以只需删一个分支上的最外层被选中的就可以。
        ArrayList<TreeNode> nodeToDel = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            TreeNode node = nodes.get(i);
            if (node.isChecked()) {
                List<TreeNode> descendant = nodes.subList(i, i + 1 + node.descendantCount);
                nodeToDel.addAll(descendant);

                //更新其爸爸的儿子数，更新其父辈的子孙数
                TreeNode ancestor = node.parent;
                if (ancestor != null) {
                    ancestor.childrenCount--;
                }

                while (ancestor != null) {
                    ancestor.descendantCount -= node.descendantCount + 1;
                    ancestor = ancestor.parent;
                }

                i += node.descendantCount;
            }
        }

        nodes.removeAll(nodeToDel);
    }
}
