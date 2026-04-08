package com.org.learning.problems;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class CompetitiveProblems {


    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int val) { this.val = val; }
    }

    static class GraphNode {
        int val;
        List<GraphNode> neighbors = new ArrayList<>();
        GraphNode(int val) { this.val = val; }
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean end;
    }

    static class Trie {
        private final TrieNode root = new TrieNode();
        void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
            cur.end = true;
        }
        boolean search(String word) {
            TrieNode node = walk(word);
            return node != null && node.end;
        }
        boolean startsWith(String prefix) {
            return walk(prefix) != null;
        }
        private TrieNode walk(String s) {
            TrieNode cur = root;
            for (char c : s.toCharArray()) {
                cur = cur.children.get(c);
                if (cur == null) return null;
            }
            return cur;
        }
    }

    static class WordDictionary {
        TrieNode root = new TrieNode();
        void addWord(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
            cur.end = true;
        }
        boolean search(String word) { return search(word, 0, root); }
        boolean search(String word, int i, TrieNode node) {
            if (i == word.length()) return node.end;
            char c = word.charAt(i);
            if (c == '.') {
                for (TrieNode child : node.children.values()) if (search(word, i + 1, child)) return true;
                return false;
            }
            TrieNode next = node.children.get(c);
            return next != null && search(word, i + 1, next);
        }
    }

    static class MedianFinder {
        PriorityQueue<Integer> small = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> large = new PriorityQueue<>();
        void addNum(int num) {
            if (small.isEmpty() || num <= small.peek()) small.offer(num); else large.offer(num);
            if (small.size() > large.size() + 1) large.offer(small.poll());
            if (large.size() > small.size()) small.offer(large.poll());
        }
        double findMedian() {
            if (small.size() == large.size()) return (small.peek() + large.peek()) / 2.0;
            return small.peek();
        }
    }

    static class Codec {
        public String encode(List<String> strs) {
            StringBuilder sb = new StringBuilder();
            for (String s : strs) sb.append(s.length()).append('#').append(s);
            return sb.toString();
        }
        public List<String> decode(String s) {
            List<String> res = new ArrayList<>();
            int i = 0;
            while (i < s.length()) {
                int j = i;
                while (s.charAt(j) != '#') j++;
                int len = Integer.parseInt(s.substring(i, j));
                res.add(s.substring(j + 1, j + 1 + len));
                i = j + 1 + len;
            }
            return res;
        }
    }

    private ListNode list(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int v : vals) {
            cur.next = new ListNode(v);
            cur = cur.next;
        }
        return dummy.next;
    }

    @Test
    @DisplayName("use hash map to instantly check for difference value, map will add index of last occurrence of a num, don’t use same element twice")
    public void twoSum() {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        Map<Integer, Integer> map = new HashMap<>();
        int[] ans = null;
        for (int i = 0; i < nums.length; i++) {
            int diff = target - nums[i];
            if (map.containsKey(diff)) {
                ans = new int[]{map.get(diff), i};
                break;
            }
            map.put(nums[i], i);
        }
        assertArrayEquals(new int[]{0, 1}, ans);
    }

    @Test
    @DisplayName("find local min and search for local max, sliding window")
    public void bestTimeToBuyAndSellStock() {
        int[] prices = {7, 1, 5, 3, 6, 4};
        int left = 0, right = 1, profit = 0;
        while (right < prices.length) {
            if (prices[right] > prices[left]) profit = Math.max(profit, prices[right] - prices[left]);
            else left = right;
            right++;
        }
        assertEquals(5, profit);
    }

    @Test
    @DisplayName("hashset to get unique values in array, to check for duplicates easily")
    public void containsDuplicate() {
        int[] nums = {1, 2, 3, 1};
        Set<Integer> set = new HashSet<>();
        boolean duplicate = false;
        for (int n : nums) if (!set.add(n)) { duplicate = true; break; }
        assertTrue(duplicate);
    }

    @Test
    @DisplayName("make two passes, first in-order, second in-reverse, to compute products")
    public void productOfArrayExceptSelf() {
        int[] nums = {1, 2, 3, 4};
        int[] res = new int[nums.length];
        int prefix = 1;
        for (int i = 0; i < nums.length; i++) {
            res[i] = prefix;
            prefix *= nums[i];
        }
        int postfix = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            res[i] *= postfix;
            postfix *= nums[i];
        }
        assertArrayEquals(new int[]{24, 12, 8, 6}, res);
    }

    @Test
    @DisplayName("pattern: prev subarray cant be negative, dynamic programming: compute max sum for each prefix")
    public void maximumSubarray() {
        int[] nums = {-2,1,-3,4,-1,2,1,-5,4};
        int max = nums[0], cur = 0;
        for (int n : nums) {
            if (cur < 0) cur = 0;
            cur += n;
            max = Math.max(max, cur);
        }
        assertEquals(6, max);
    }

    @Test
    @DisplayName("dp: compute max and max-abs-val for each prefix subarr")
    public void maximumProductSubarray() {
        int[] nums = {2, 3, -2, 4};
        int res = nums[0], curMax = 1, curMin = 1;
        for (int n : nums) {
            int tmp = curMax * n;
            curMax = Math.max(n, Math.max(tmp, curMin * n));
            curMin = Math.min(n, Math.min(tmp, curMin * n));
            res = Math.max(res, curMax);
        }
        assertEquals(6, res);
    }

    @Test
    @DisplayName("check if half of array is sorted in order to find pivot, arr is guaranteed to be in at most two sorted subarrays")
    public void findMinimumInRotatedSortedArray() {
        int[] nums = {3,4,5,1,2};
        int l = 0, r = nums.length - 1, ans = nums[0];
        while (l <= r) {
            if (nums[l] < nums[r]) { ans = Math.min(ans, nums[l]); break; }
            int m = l + (r - l) / 2;
            ans = Math.min(ans, nums[m]);
            if (nums[m] >= nums[l]) l = m + 1; else r = m - 1;
        }
        assertEquals(1, ans);
    }

    @Test
    @DisplayName("at most two sorted halfs, mid will be apart of left sorted or right sorted, if target is in range of sorted portion then search it, otherwise search other half")
    public void searchInRotatedSortedArray() {
        int[] nums = {4,5,6,7,0,1,2};
        int target = 0, l = 0, r = nums.length - 1, found = -1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (nums[m] == target) { found = m; break; }
            if (nums[l] <= nums[m]) {
                if (target >= nums[l] && target < nums[m]) r = m - 1;
                else l = m + 1;
            } else {
                if (target > nums[m] && target <= nums[r]) l = m + 1;
                else r = m - 1;
            }
        }
        assertEquals(4, found);
    }

    @Test
    @DisplayName("sort input, for each first element, find next two where -a = b+c, if a=prevA, skip a, if b=prevB skip b to elim duplicates; to find b,c use two pointers, left/right on remaining list")
    public void threeSum() {
        int[] nums = {-1,0,1,2,-1,-4};
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum > 0) r--;
                else if (sum < 0) l++;
                else {
                    res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    l++;
                    while (l < r && nums[l] == nums[l - 1]) l++;
                }
            }
        }
        assertEquals(2, res.size());
    }

    @Test
    @DisplayName("shrinking window, left/right initially at endpoints, shift the pointer with min height")
    public void containerWithMostWater() {
        int[] h = {1,8,6,2,5,4,8,3,7};
        int l = 0, r = h.length - 1, max = 0;
        while (l < r) {
            max = Math.max(max, (r - l) * Math.min(h[l], h[r]));
            if (h[l] < h[r]) l++; else r--;
        }
        assertEquals(49, max);
    }

    @Test
    @DisplayName("add bit by bit, be mindful of carry, after adding, if carry is still 1, then add it as well")
    public void sumOfTwoIntegers() {
        int a = 2, b = 3;
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        assertEquals(5, a);
    }

    @Test
    @DisplayName("modulo, and dividing n; mod and div are expensive, to divide use bit shift, instead of mod to get 1's place use bitwise & 1")
    public void numberOf1Bits() {
        int n = 11, count = 0;
        while (n != 0) {
            count += (n & 1);
            n >>>= 1;
        }
        assertEquals(3, count);
    }

    @Test
    @DisplayName("write out result for num=16 to figure out pattern; res[i] = res[i - offset], where offset is the biggest power of 2 <= I")
    public void countingBits() {
        int n = 5;
        int[] res = new int[n + 1];
        int offset = 1;
        for (int i = 1; i <= n; i++) {
            if (offset * 2 == i) offset = i;
            res[i] = 1 + res[i - offset];
        }
        assertArrayEquals(new int[]{0,1,1,2,1,2}, res);
    }

    @Test
    @DisplayName("compute expected sum - real sum; xor n with each index and value")
    public void missingNumber() {
        int[] nums = {3,0,1};
        int missing = nums.length;
        for (int i = 0; i < nums.length; i++) missing ^= i ^ nums[i];
        assertEquals(2, missing);
    }

    @Test
    @DisplayName("reverse each of 32 bits")
    public void reverseBits() {
        int n = 43261596;
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res <<= 1;
            res |= (n & 1);
            n >>>= 1;
        }
        assertEquals(964176192, res);
    }

    @Test
    @DisplayName("subproblem find (n-1) and (n-2), sum = n")
    public void climbingStairs() {
        int n = 3;
        int one = 1, two = 1;
        for (int i = 0; i < n - 1; i++) {
            int temp = one;
            one = one + two;
            two = temp;
        }
        assertEquals(3, one);
    }

    @Test
    @DisplayName("top-down: recursive dfs, for amount, branch for each coin, cache to store prev coin_count for each amount; bottom-up: compute coins for amount = 1, up until n, using for each coin (amount - coin), cache prev values")
    public void coinChange() {
        int[] coins = {1,2,5};
        int amount = 11;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) if (a - c >= 0) dp[a] = Math.min(dp[a], 1 + dp[a - c]);
        }
        assertEquals(3, dp[amount]);
    }

    @Test
    @DisplayName("recursive: foreach num, get subseq with num and without num, only include num if prev was less, cache solution of each; dp=subseq length which must end with each num, curr num must be after a prev dp or by itself")
    public void longestIncreasingSubsequence() {
        int[] nums = {10,9,2,5,3,7,101,18};
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int i = 0, j = size;
            while (i != j) {
                int m = (i + j) / 2;
                if (tails[m] < x) i = m + 1; else j = m;
            }
            tails[i] = x;
            if (i == size) ++size;
        }
        assertEquals(4, size);
    }

    @Test
    @DisplayName("recursive: if first chars are equal find lcs of remaining of each, else max of: lcs of first and remain of 2nd and lcs of 2nd remain of first, cache result; nested forloop to compute the cache without recursion")
    public void longestCommonSubsequence() {
        String a = "abcde", b = "ace";
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = a.length() - 1; i >= 0; i--) {
            for (int j = b.length() - 1; j >= 0; j--) {
                if (a.charAt(i) == b.charAt(j)) dp[i][j] = 1 + dp[i + 1][j + 1];
                else dp[i][j] = Math.max(dp[i + 1][j], dp[i][j + 1]);
            }
        }
        assertEquals(3, dp[0][0]);
    }

    @Test
    @DisplayName("for each prefix, if prefix is in dict and wordbreak(remaining str)=True, then return True, cache result of wordbreak")
    public void wordBreak() {
        String s = "leetcode";
        Set<String> dict = new HashSet<>(Arrays.asList("leet", "code"));
        boolean[] dp = new boolean[s.length() + 1];
        dp[s.length()] = true;
        for (int i = s.length() - 1; i >= 0; i--) {
            for (String w : dict) {
                if (i + w.length() <= s.length() && s.substring(i, i + w.length()).equals(w)) {
                    dp[i] = dp[i + w.length()];
                }
                if (dp[i]) break;
            }
        }
        assertTrue(dp[0]);
    }

    @Test
    @DisplayName("visualize the decision tree, base case is curSum = or > target, each candidate can have children of itself or elements to right of it inorder to elim duplicate solutions")
    public void combinationSum() {
        int[] candidates = {2,3,6,7};
        int target = 7;
        List<List<Integer>> res = new ArrayList<>();
        backtrackCombinationSum(candidates, target, 0, new ArrayList<>(), res);
        assertTrue(res.stream().anyMatch(l -> l.equals(Arrays.asList(7))));
        assertTrue(res.stream().anyMatch(l -> l.equals(Arrays.asList(2,2,3))));
    }

    private void backtrackCombinationSum(int[] c, int target, int i, List<Integer> cur, List<List<Integer>> res) {
        if (target == 0) { res.add(new ArrayList<>(cur)); return; }
        if (i >= c.length || target < 0) return;
        cur.add(c[i]);
        backtrackCombinationSum(c, target - c[i], i, cur, res);
        cur.remove(cur.size() - 1);
        backtrackCombinationSum(c, target, i + 1, cur, res);
    }

    @Test
    @DisplayName("for each num, get max of prev subarr, or num + prev subarr not including last element, store results of prev, and prev not including last element")
    public void houseRobber() {
        int[] nums = {2,7,9,3,1};
        int rob1 = 0, rob2 = 0;
        for (int n : nums) {
            int temp = Math.max(n + rob1, rob2);
            rob1 = rob2;
            rob2 = temp;
        }
        assertEquals(12, rob2);
    }

    @Test
    @DisplayName("subarr = arr without first & last, get max of subarr, then pick which of first/last should be added to it")
    public void houseRobberII() {
        int[] nums = {2,3,2};
        int ans = Math.max(robLinear(Arrays.copyOfRange(nums, 0, nums.length - 1)), robLinear(Arrays.copyOfRange(nums, 1, nums.length)));
        assertEquals(3, ans);
    }

    private int robLinear(int[] nums) {
        int rob1 = 0, rob2 = 0;
        for (int n : nums) {
            int temp = Math.max(n + rob1, rob2);
            rob1 = rob2;
            rob2 = temp;
        }
        return rob2;
    }

    @Test
    @DisplayName("can cur char be decoded in one or two ways? Recursion -> cache -> iterative dp solution, a lot of edge cases to determine, 52, 31, 29, 10, 20 only decoded one way, 11, 26 decoded two ways")
    public void decodeWays() {
        String s = "12";
        int[] dp = new int[s.length() + 1];
        dp[s.length()] = 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '0') continue;
            dp[i] = dp[i + 1];
            if (i + 1 < s.length() && (s.charAt(i) == '1' || (s.charAt(i) == '2' && s.charAt(i + 1) < '7'))) dp[i] += dp[i + 2];
        }
        assertEquals(2, dp[0]);
    }

    @Test
    @DisplayName("work backwards from solution, store paths for each position in grid, to further optimize, we don’t store whole grid, only need to store prev row")
    public void uniquePaths() {
        int m = 3, n = 7;
        int[] row = new int[n];
        Arrays.fill(row, 1);
        for (int i = 0; i < m - 1; i++) {
            int[] newRow = new int[n];
            Arrays.fill(newRow, 1);
            for (int j = n - 2; j >= 0; j--) newRow[j] = newRow[j + 1] + row[j];
            row = newRow;
        }
        assertEquals(28, row[0]);
    }

    @Test
    @DisplayName("visualize the recursive tree, cache solution for O(n) time/mem complexity, iterative is O(1) mem, just iterate backwards to see if element can reach goal node, if yes, then set it equal to goal node, continue")
    public void jumpGame() {
        int[] nums = {2,3,1,1,4};
        int goal = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) if (i + nums[i] >= goal) goal = i;
        assertEquals(0, goal);
    }

    @Test
    @DisplayName("recursive dfs, hashmap for visited nodes")
    public void cloneGraph() {
        GraphNode a = new GraphNode(1);
        GraphNode b = new GraphNode(2);
        a.neighbors.add(b); b.neighbors.add(a);
        Map<GraphNode, GraphNode> map = new HashMap<>();
        GraphNode clone = clone(a, map);
        assertNotSame(a, clone);
        assertEquals(1, clone.val);
        assertEquals(2, clone.neighbors.get(0).val);
    }

    private GraphNode clone(GraphNode node, Map<GraphNode, GraphNode> map) {
        if (node == null) return null;
        if (map.containsKey(node)) return map.get(node);
        GraphNode copy = new GraphNode(node.val);
        map.put(node, copy);
        for (GraphNode nei : node.neighbors) copy.neighbors.add(clone(nei, map));
        return copy;
    }

    @Test
    @DisplayName("build adjacentcy_list with edges, run dfs on each V, if while dfs on V we see V again, then loop exists, otherwise V isnt in a loop, 3 states= not visited, visited, still visiting")
    public void courseSchedule() {
        int numCourses = 2;
        int[][] prerequisites = {{1,0}};
        Map<Integer, List<Integer>> pre = new HashMap<>();
        for (int i = 0; i < numCourses; i++) pre.put(i, new ArrayList<>());
        for (int[] p : prerequisites) pre.get(p[0]).add(p[1]);
        Set<Integer> visiting = new HashSet<>();
        Set<Integer> done = new HashSet<>();
        boolean ok = true;
        for (int c = 0; c < numCourses; c++) if (!canFinish(c, pre, visiting, done)) ok = false;
        assertTrue(ok);
    }

    private boolean canFinish(int c, Map<Integer, List<Integer>> pre, Set<Integer> visiting, Set<Integer> done) {
        if (done.contains(c)) return true;
        if (visiting.contains(c)) return false;
        visiting.add(c);
        for (int p : pre.get(c)) if (!canFinish(p, pre, visiting, done)) return false;
        visiting.remove(c);
        done.add(c);
        return true;
    }

    @Test
    @DisplayName("dfs each cell, keep track of visited, and track which reach pac, atl; dfs on cells adjacent to pac, atl, find overlap of cells that are visited by both pac and atl cells")
    public void pacificAtlanticWaterFlow() {
        int[][] heights = {{1,2,2,3,5},{3,2,3,4,4},{2,4,5,3,1},{6,7,1,4,5},{5,1,1,2,4}};
        int rows = heights.length, cols = heights[0].length;
        boolean[][] pac = new boolean[rows][cols], atl = new boolean[rows][cols];
        for (int c = 0; c < cols; c++) { flow(0, c, pac, heights, Integer.MIN_VALUE); flow(rows - 1, c, atl, heights, Integer.MIN_VALUE); }
        for (int r = 0; r < rows; r++) { flow(r, 0, pac, heights, Integer.MIN_VALUE); flow(r, cols - 1, atl, heights, Integer.MIN_VALUE); }
        int overlap = 0;
        for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++) if (pac[r][c] && atl[r][c]) overlap++;
        assertTrue(overlap > 0);
    }

    private void flow(int r, int c, boolean[][] vis, int[][] h, int prev) {
        if (r < 0 || c < 0 || r == h.length || c == h[0].length || vis[r][c] || h[r][c] < prev) return;
        vis[r][c] = true;
        flow(r + 1, c, vis, h, h[r][c]);
        flow(r - 1, c, vis, h, h[r][c]);
        flow(r, c + 1, vis, h, h[r][c]);
        flow(r, c - 1, vis, h, h[r][c]);
    }

    @Test
    @DisplayName("foreach cell, if cell is 1 and unvisited run dfs, increment cound and marking each contigous 1 as visited")
    public void numberOfIslands() {
        char[][] grid = {{'1','1','0'},{'0','1','0'},{'1','0','1'}};
        int count = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    sink(grid, r, c);
                }
            }
        }
        assertEquals(3, count);
    }

    private void sink(char[][] g, int r, int c) {
        if (r < 0 || c < 0 || r == g.length || c == g[0].length || g[r][c] == '0') return;
        g[r][c] = '0';
        sink(g, r + 1, c); sink(g, r - 1, c); sink(g, r, c + 1); sink(g, r, c - 1);
    }

    @Test
    @DisplayName("use bruteforce and try to optimize, consider the max subseq containing each num; add each num to hashset, for each num if num-1 doesn’t exist, count the consecutive nums after num, ie num+1; there is also a union-find solution")
    public void longestConsecutiveSequence() {
        int[] nums = {100,4,200,1,3,2};
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);
        int longest = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) {
                int len = 1;
                while (set.contains(n + len)) len++;
                longest = Math.max(longest, len);
            }
        }
        assertEquals(4, longest);
    }

    @Test
    @DisplayName("chars of a word not in order, the words are in order, find adjacency list of each unique char by iterating through adjacent words and finding first chars that are different, run topsort on graph and do loop detection")
    public void alienDictionary() {
        String[] words = {"wrt","wrf","er","ett","rftt"};
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();
        for (String w : words) for (char c : w.toCharArray()) { adj.putIfAbsent(c, new HashSet<>()); indegree.putIfAbsent(c, 0); }
        for (int i = 0; i < words.length - 1; i++) {
            String a = words[i], b = words[i + 1];
            int len = Math.min(a.length(), b.length());
            for (int j = 0; j < len; j++) {
                if (a.charAt(j) != b.charAt(j)) {
                    if (adj.get(a.charAt(j)).add(b.charAt(j))) indegree.put(b.charAt(j), indegree.get(b.charAt(j)) + 1);
                    break;
                }
            }
        }
        Queue<Character> q = new ArrayDeque<>();
        for (Map.Entry<Character, Integer> e : indegree.entrySet()) if (e.getValue() == 0) q.offer(e.getKey());
        StringBuilder sb = new StringBuilder();
        while (!q.isEmpty()) {
            char c = q.poll();
            sb.append(c);
            for (char nei : adj.get(c)) {
                indegree.put(nei, indegree.get(nei) - 1);
                if (indegree.get(nei) == 0) q.offer(nei);
            }
        }
        assertEquals("wertf", sb.toString());
    }

    @Test
    @DisplayName("union find, if union return false, loop exists, at end size must equal n, or its not connected; dfs to get size and check for loop, since each edge is double, before dfs on neighbor of N, remove N from neighbor list of neighbor")
    public void graphValidTree() {
        int n = 5;
        int[][] edges = {{0,1},{0,2},{0,3},{1,4}};
        int[] parent = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
        boolean valid = true;
        for (int[] e : edges) if (!union(e[0], e[1], parent, rank)) valid = false;
        assertTrue(valid && edges.length == n - 1);
    }

    private int find(int x, int[] parent) {
        if (parent[x] != x) parent[x] = find(parent[x], parent);
        return parent[x];
    }

    private boolean union(int a, int b, int[] parent, int[] rank) {
        int pa = find(a, parent), pb = find(b, parent);
        if (pa == pb) return false;
        if (rank[pa] < rank[pb]) parent[pa] = pb;
        else if (rank[pb] < rank[pa]) parent[pb] = pa;
        else { parent[pb] = pa; rank[pa]++; }
        return true;
    }

    @Test
    @DisplayName("dfs on each node that hasn’t been visited, increment component count, adjacency list; bfs and union find are possible")
    public void numberOfConnectedComponents() {
        int n = 5;
        int[][] edges = {{0,1},{1,2},{3,4}};
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
        boolean[] vis = new boolean[n];
        int comps = 0;
        for (int i = 0; i < n; i++) if (!vis[i]) { comps++; dfsComp(i, adj, vis); }
        assertEquals(2, comps);
    }

    private void dfsComp(int node, List<List<Integer>> adj, boolean[] vis) {
        if (vis[node]) return;
        vis[node] = true;
        for (int nei : adj.get(node)) dfsComp(nei, adj, vis);
    }

    @Test
    @DisplayName("insert new interval in order, then merge intervals; newinterval could only merge with one interval that comes before it, then add remaining intervals")
    public void insertInterval() {
        int[][] intervals = {{1,3},{6,9}};
        int[] newInterval = {2,5};
        List<int[]> res = new ArrayList<>();
        int i = 0;
        while (i < intervals.length && intervals[i][1] < newInterval[0]) res.add(intervals[i++]);
        while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }
        res.add(newInterval);
        while (i < intervals.length) res.add(intervals[i++]);
        assertArrayEquals(new int[][]{{1,5},{6,9}}, res.toArray(new int[0][]));
    }

    @Test
    @DisplayName("sort each interval, overlapping intervals should be adjacent, iterate and build solution; also graph method, less efficient, more complicated")
    public void mergeIntervals() {
        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> out = new ArrayList<>();
        int[] cur = intervals[0];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] <= cur[1]) cur[1] = Math.max(cur[1], intervals[i][1]);
            else { out.add(cur); cur = intervals[i]; }
        }
        out.add(cur);
        assertArrayEquals(new int[][]{{1,6},{8,10},{15,18}}, out.toArray(new int[0][]));
    }

    @Test
    @DisplayName("instead of removing, count how max num of intervals you can include, sort intervals, dp to compute max intervals up until the i-th interval")
    public void nonOverlappingIntervals() {
        int[][] intervals = {{1,2},{2,3},{3,4},{1,3}};
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[1]));
        int end = intervals[0][1], count = 0;
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < end) count++;
            else end = intervals[i][1];
        }
        assertEquals(1, count);
    }

    @Test
    @DisplayName("sort intervals by start time, if second interval doesn’t overlap with first, then third def wont overlap with first")
    public void meetingRooms() {
        int[][] intervals = {{0,30},{5,10},{15,20}};
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        boolean canAttend = true;
        for (int i = 1; i < intervals.length; i++) if (intervals[i][0] < intervals[i - 1][1]) canAttend = false;
        assertFalse(canAttend);
    }

    @Test
    @DisplayName("we care about the points in time where we are starting/ending a meeting, we already are given those, just separate start/end and traverse counting num of meetings going at these points in time; for each meeting check if a prev meeting has finished before curr started, using min heap")
    public void meetingRoomsII() {
        int[][] intervals = {{0,30},{5,10},{15,20}};
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        for (int[] interval : intervals) {
            if (!heap.isEmpty() && interval[0] >= heap.peek()) heap.poll();
            heap.offer(interval[1]);
        }
        assertEquals(2, heap.size());
    }

    @Test
    @DisplayName("iterate through maintaining cur and prev; recursively reverse, return new head of list")
    public void reverseALinkedList() {
        ListNode head = list(1,2,3,4,5);
        ListNode prev = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        assertEquals(5, prev.val);
        assertEquals(4, prev.next.val);
    }

    @Test
    @DisplayName("dict to remember visited nodes; two pointers at different speeds, if they meet there is loop")
    public void detectCycleInALinkedList() {
        ListNode a = new ListNode(3), b = new ListNode(2), c = new ListNode(0), d = new ListNode(-4);
        a.next = b; b.next = c; c.next = d; d.next = b;
        ListNode slow = a, fast = a;
        boolean cycle = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) { cycle = true; break; }
        }
        assertTrue(cycle);
    }

    @Test
    @DisplayName("insert each node from one list into the other")
    public void mergeTwoSortedLists() {
        ListNode a = list(1,2,4), b = list(1,3,4), dummy = new ListNode(0), tail = dummy;
        while (a != null && b != null) {
            if (a.val < b.val) { tail.next = a; a = a.next; }
            else { tail.next = b; b = b.next; }
            tail = tail.next;
        }
        tail.next = (a != null) ? a : b;
        assertEquals(1, dummy.next.val);
        assertEquals(1, dummy.next.next.val);
    }

    @Test
    @DisplayName("divied and conquer, merge lists, N totalnodes, k-lists, O(N*logk). For each list, find min val, insert it into list, use priorityQ to optimize finding min O(N*logk)")
    public void mergeKSortedLists() {
        ListNode[] lists = {list(1,4,5), list(1,3,4), list(2,6)};
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode node : lists) if (node != null) pq.offer(node);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            tail.next = node;
            tail = tail.next;
            if (node.next != null) pq.offer(node.next);
        }
        assertEquals(1, dummy.next.val);
        assertEquals(1, dummy.next.next.val);
    }

    @Test
    @DisplayName("use dummy node at head of list, compute len of list; two pointers, second has offset of n from first")
    public void removeNthNodeFromEndOfList() {
        ListNode dummy = new ListNode(0);
        dummy.next = list(1,2,3,4,5);
        int n = 2;
        ListNode left = dummy, right = dummy;
        while (n-- >= 0) right = right.next;
        while (right != null) { left = left.next; right = right.next; }
        left.next = left.next.next;
        assertEquals(1, dummy.next.val);
        assertEquals(2, dummy.next.next.val);
        assertEquals(3, dummy.next.next.next.val);
        assertEquals(5, dummy.next.next.next.next.val);
    }

    @Test
    @DisplayName("reverse second half of list, then easily reorder it; non-optimal way is to store list in array")
    public void reorderList() {
        ListNode head = list(1,2,3,4);
        ListNode slow = head, fast = head.next;
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode second = slow.next; slow.next = null;
        ListNode prev = null;
        while (second != null) {
            ListNode next = second.next;
            second.next = prev;
            prev = second;
            second = next;
        }
        ListNode first = head; second = prev;
        while (second != null) {
            ListNode t1 = first.next, t2 = second.next;
            first.next = second;
            second.next = t1;
            first = t1;
            second = t2;
        }
        assertEquals(1, head.val);
        assertEquals(4, head.next.val);
        assertEquals(2, head.next.next.val);
        assertEquals(3, head.next.next.next.val);
    }

    @Test
    @DisplayName("use sets to keep track of all rows, cols to zero out, after, for each num if it is in a zero row or col then change it to 0; flag first cell in row, and col to mark row/col that needs to be zeroed")
    public void setMatrixZeroes() {
        int[][] matrix = {{1,1,1},{1,0,1},{1,1,1}};
        boolean rowZero = false;
        for (int c = 0; c < matrix[0].length; c++) if (matrix[0][c] == 0) rowZero = true;
        for (int r = 1; r < matrix.length; r++) {
            for (int c = 0; c < matrix[0].length; c++) {
                if (matrix[r][c] == 0) {
                    matrix[0][c] = 0;
                    matrix[r][0] = 0;
                }
            }
        }
        for (int r = 1; r < matrix.length; r++) {
            for (int c = 1; c < matrix[0].length; c++) {
                if (matrix[0][c] == 0 || matrix[r][0] == 0) matrix[r][c] = 0;
            }
        }
        if (matrix[0][0] == 0) for (int r = 0; r < matrix.length; r++) matrix[r][0] = 0;
        if (rowZero) for (int c = 0; c < matrix[0].length; c++) matrix[0][c] = 0;
        assertArrayEquals(new int[]{1,0,1}, matrix[0]);
        assertArrayEquals(new int[]{0,0,0}, matrix[1]);
        assertArrayEquals(new int[]{1,0,1}, matrix[2]);
    }

    @Test
    @DisplayName("keep track of visited cells; keep track of boundaries, layer-by-layer")
    public void spiralMatrix() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        List<Integer> res = new ArrayList<>();
        int left = 0, right = matrix[0].length, top = 0, bottom = matrix.length;
        while (left < right && top < bottom) {
            for (int i = left; i < right; i++) res.add(matrix[top][i]);
            top++;
            for (int i = top; i < bottom; i++) res.add(matrix[i][right - 1]);
            right--;
            if (!(left < right && top < bottom)) break;
            for (int i = right - 1; i >= left; i--) res.add(matrix[bottom - 1][i]);
            bottom--;
            for (int i = bottom - 1; i >= top; i--) res.add(matrix[i][left]);
            left++;
        }
        assertEquals(Arrays.asList(1,2,3,6,9,8,7,4,5), res);
    }

    @Test
    @DisplayName("rotate layer-by-layer, use that it's a square as advantage, rotate positions in reverse order, store a in temp, a = b, b = c, c = d, d = temp")
    public void rotateImage() {
        int[][] matrix = {{1,2,3},{4,5,6},{7,8,9}};
        int l = 0, r = matrix.length - 1;
        while (l < r) {
            for (int i = 0; i < r - l; i++) {
                int top = l, bottom = r;
                int topLeft = matrix[top][l + i];
                matrix[top][l + i] = matrix[bottom - i][l];
                matrix[bottom - i][l] = matrix[bottom][r - i];
                matrix[bottom][r - i] = matrix[top + i][r];
                matrix[top + i][r] = topLeft;
            }
            l++; r--;
        }
        assertArrayEquals(new int[]{7,4,1}, matrix[0]);
        assertArrayEquals(new int[]{8,5,2}, matrix[1]);
        assertArrayEquals(new int[]{9,6,3}, matrix[2]);
    }

    @Test
    @DisplayName("dfs on each cell, for each search remember visited cells, and remove cur visited cell right before you return from dfs")
    public void wordSearch() {
        char[][] board = {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        boolean found = false;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (exist(board, "ABCCED", r, c, 0)) found = true;
            }
        }
        assertTrue(found);
    }

    private boolean exist(char[][] board, String word, int r, int c, int i) {
        if (i == word.length()) return true;
        if (r < 0 || c < 0 || r == board.length || c == board[0].length || board[r][c] != word.charAt(i)) return false;
        char temp = board[r][c];
        board[r][c] = '#';
        boolean found = exist(board, word, r + 1, c, i + 1) || exist(board, word, r - 1, c, i + 1) || exist(board, word, r, c + 1, i + 1) || exist(board, word, r, c - 1, i + 1);
        board[r][c] = temp;
        return found;
    }

    @Test
    @DisplayName("sliding window, if we see same char twice within curr window, shift start position")
    public void longestSubstringWithoutRepeatingCharacters() {
        String s = "abcabcbb";
        Set<Character> set = new HashSet<>();
        int l = 0, max = 0;
        for (int r = 0; r < s.length(); r++) {
            while (set.contains(s.charAt(r))) set.remove(s.charAt(l++));
            set.add(s.charAt(r));
            max = Math.max(max, r - l + 1);
        }
        assertEquals(3, max);
    }

    @Test
    @DisplayName("PAY ATTENTION: limited to chars A-Z; for each capital char, check if it could create the longest repeating substr, use sliding window to optimize; check if windowlen=1 works, if yes, increment len, if not, shift window right")
    public void longestRepeatingCharacterReplacement() {
        String s = "ABAB"; int k = 2;
        int[] count = new int[26];
        int l = 0, maxf = 0, res = 0;
        for (int r = 0; r < s.length(); r++) {
            maxf = Math.max(maxf, ++count[s.charAt(r) - 'A']);
            while ((r - l + 1) - maxf > k) count[s.charAt(l++) - 'A']--;
            res = Math.max(res, r - l + 1);
        }
        assertEquals(4, res);
    }

    @Test
    @DisplayName("need is num of unique char in T, HAVE is num of char we have valid count for, sliding window, move right until valid, if valid, increment left until invalid, to check validity keep track if the count of each unique char is satisfied")
    public void minimumWindowSubstring() {
        String s = "ADOBECODEBANC", t = "ABC";
        Map<Character, Integer> need = new HashMap<>(), window = new HashMap<>();
        for (char c : t.toCharArray()) need.put(c, need.getOrDefault(c, 0) + 1);
        int have = 0, needCount = need.size(), l = 0;
        int[] res = {-1, -1};
        int resLen = Integer.MAX_VALUE;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            window.put(c, window.getOrDefault(c, 0) + 1);
            if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) have++;
            while (have == needCount) {
                if ((r - l + 1) < resLen) { resLen = r - l + 1; res[0] = l; res[1] = r; }
                char leftChar = s.charAt(l);
                window.put(leftChar, window.get(leftChar) - 1);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) have--;
                l++;
            }
        }
        assertEquals("BANC", s.substring(res[0], res[1] + 1));
    }

    @Test
    @DisplayName("hashmap to count each char in str1, decrement for str2")
    public void validAnagram() {
        String s = "anagram", t = "nagaram";
        int[] count = new int[26];
        for (char c : s.toCharArray()) count[c - 'a']++;
        for (char c : t.toCharArray()) count[c - 'a']--;
        boolean ok = true;
        for (int n : count) if (n != 0) ok = false;
        assertTrue(ok);
    }

    @Test
    @DisplayName("for each of 26 chars, use count of each char in each word as tuple for key in dict, value is the list of anagrams")
    public void groupAnagrams() {
        String[] strs = {"eat","tea","tan","ate","nat","bat"};
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            int[] count = new int[26];
            for (char c : s.toCharArray()) count[c - 'a']++;
            String key = Arrays.toString(count);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        assertEquals(3, map.size());
    }

    @Test
    @DisplayName("push opening brace on stack, pop if matching close brace, at end if stack empty, return true")
    public void validParentheses() {
        String s = "()[]{}";
        Map<Character, Character> map = Map.of(')', '(', ']', '[', '}', '{');
        Deque<Character> stack = new ArrayDeque<>();
        boolean valid = true;
        for (char c : s.toCharArray()) {
            if (map.containsValue(c)) stack.push(c);
            else if (stack.isEmpty() || stack.pop() != map.get(c)) valid = false;
        }
        assertTrue(valid && stack.isEmpty());
    }

    @Test
    @DisplayName("left, right pointers, update left and right until each points at alphanum, compare left and right, continue until left >= right, don’t distinguish between upper/lowercase")
    public void validPalindrome() {
        String s = "A man, a plan, a canal: Panama";
        int l = 0, r = s.length() - 1;
        boolean valid = true;
        while (l < r) {
            while (l < r && !Character.isLetterOrDigit(s.charAt(l))) l++;
            while (l < r && !Character.isLetterOrDigit(s.charAt(r))) r--;
            if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) { valid = false; break; }
            l++; r--;
        }
        assertTrue(valid);
    }

    @Test
    @DisplayName("foreach char in str, consider it were the middle, consider if pali was odd or even")
    public void longestPalindromicSubstring() {
        String s = "babad";
        String best = "";
        for (int i = 0; i < s.length(); i++) {
            String odd = expand(s, i, i);
            String even = expand(s, i, i + 1);
            if (odd.length() > best.length()) best = odd;
            if (even.length() > best.length()) best = even;
        }
        assertTrue(best.equals("bab") || best.equals("aba"));
    }

    private String expand(String s, int l, int r) {
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
        return s.substring(l + 1, r);
    }

    @Test
    @DisplayName("same as longest palindromic string, each char in str as middle and expand outwards, do same for pali of even len; maybe read up on manachers alg")
    public void palindromicSubstrings() {
        String s = "aaa";
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            count += countPal(s, i, i);
            count += countPal(s, i, i + 1);
        }
        assertEquals(6, count);
    }

    private int countPal(String s, int l, int r) {
        int count = 0;
        while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
            count++;
            l--; r++;
        }
        return count;
    }

    @Test
    @DisplayName("store length of str before each string and delimiter like '#'")
    public void encodeAndDecodeStrings() {
        Codec codec = new Codec();
        List<String> input = Arrays.asList("lint", "code", "love", "you");
        assertEquals(input, codec.decode(codec.encode(input)));
    }

    @Test
    @DisplayName("recursive dfs to find max-depth of subtrees; iterative bfs to count number of levels in tree")
    public void maximumDepthOfBinaryTree() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        assertEquals(3, depth(root));
    }

    private int depth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(depth(root.left), depth(root.right));
    }

    @Test
    @DisplayName("recursive dfs on both trees at the same time; iterative bfs compare each level of both trees")
    public void sameTree() {
        TreeNode p = new TreeNode(1); p.left = new TreeNode(2); p.right = new TreeNode(3);
        TreeNode q = new TreeNode(1); q.left = new TreeNode(2); q.right = new TreeNode(3);
        assertTrue(same(p, q));
    }

    private boolean same(TreeNode a, TreeNode b) {
        if (a == null && b == null) return true;
        if (a == null || b == null || a.val != b.val) return false;
        return same(a.left, b.left) && same(a.right, b.right);
    }

    @Test
    @DisplayName("recursive dfs to invert subtrees; bfs to invert levels, use collections.deque; iterative dfs is easy with stack if doing pre-order traversal")
    public void invertFlipBinaryTree() {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2); root.right = new TreeNode(7);
        root.left.left = new TreeNode(1); root.left.right = new TreeNode(3);
        root.right.left = new TreeNode(6); root.right.right = new TreeNode(9);
        invert(root);
        assertEquals(7, root.left.val);
        assertEquals(2, root.right.val);
    }

    private TreeNode invert(TreeNode root) {
        if (root == null) return null;
        TreeNode temp = root.left;
        root.left = invert(root.right);
        root.right = invert(temp);
        return root;
    }

    @Test
    @DisplayName("helper returns maxpathsum without splitting branches, inside helper we also update maxSum by computing maxpathsum WITH a split")
    public void binaryTreeMaximumPathSum() {
        TreeNode root = new TreeNode(-10);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        int[] max = {Integer.MIN_VALUE};
        maxGain(root, max);
        assertEquals(42, max[0]);
    }

    private int maxGain(TreeNode node, int[] max) {
        if (node == null) return 0;
        int left = Math.max(maxGain(node.left, max), 0);
        int right = Math.max(maxGain(node.right, max), 0);
        max[0] = Math.max(max[0], node.val + left + right);
        return node.val + Math.max(left, right);
    }

    @Test
    @DisplayName("iterative bfs, add prev level which doesn't have any nulls to the result")
    public void binaryTreeLevelOrderTraversal() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        List<List<Integer>> levels = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = q.poll();
                level.add(node.val);
                if (node.left != null) q.offer(node.left);
                if (node.right != null) q.offer(node.right);
            }
            levels.add(level);
        }
        assertEquals(Arrays.asList(3), levels.get(0));
        assertEquals(Arrays.asList(9,20), levels.get(1));
    }

    @Test
    @DisplayName("bfs every single non-null node is added to string, and it's children are added too, even if they're null, deserialize by adding each non-null node to queue, deque node, it's children are next two nodes in string")
    public void serializeAndDeserializeBinaryTree() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        String serialized = serialize(root);
        TreeNode deserialized = deserialize(serialized);
        assertEquals(1, deserialized.val);
        assertEquals(2, deserialized.left.val);
        assertEquals(3, deserialized.right.val);
    }

    private String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode node = q.poll();
            if (node == null) { sb.append("N,"); continue; }
            sb.append(node.val).append(',');
            if (node.left != null) q.offer(node.left); else sb.append("N,");
            if (node.right != null) q.offer(node.right); else sb.append("N,");
        }
        return sb.toString();
    }

    private TreeNode deserialize(String data) {
        String[] vals = data.split(",");
        if (vals[0].equals("N")) return null;
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
        Queue<TreeNode> q = new ArrayDeque<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && !vals[i].equals("N")) {
                node.left = new TreeNode(Integer.parseInt(vals[i]));
                q.offer(node.left);
            }
            i++;
            if (i < vals.length && !vals[i].equals("N")) {
                node.right = new TreeNode(Integer.parseInt(vals[i]));
                q.offer(node.right);
            }
            i++;
        }
        return root;
    }

    @Test
    @DisplayName("traverse s to check if any subtree in s equals t; merkle hashing?")
    public void subtreeOfAnotherTree() {
        TreeNode s = new TreeNode(3);
        s.left = new TreeNode(4); s.right = new TreeNode(5);
        s.left.left = new TreeNode(1); s.left.right = new TreeNode(2);
        TreeNode t = new TreeNode(4);
        t.left = new TreeNode(1); t.right = new TreeNode(2);
        assertTrue(isSubtree(s, t));
    }

    private boolean isSubtree(TreeNode s, TreeNode t) {
        if (t == null) return true;
        if (s == null) return false;
        if (same(s, t)) return true;
        return isSubtree(s.left, t) || isSubtree(s.right, t);
    }

    @Test
    @DisplayName("first element in pre-order is root, elements left of root in in-order are left subtree, right of root are right subtree, recursively build subtrees")
    public void constructBinaryTreeFromPreorderAndInorderTraversal() {
        int[] preorder = {3,9,20,15,7};
        int[] inorder = {9,3,15,20,7};
        Map<Integer, Integer> idx = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) idx.put(inorder[i], i);
        TreeNode root = build(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1, idx);
        assertEquals(3, root.val);
        assertEquals(9, root.left.val);
        assertEquals(20, root.right.val);
    }

    private TreeNode build(int[] pre, int ps, int pe, int[] in, int is, int ie, Map<Integer, Integer> idx) {
        if (ps > pe || is > ie) return null;
        TreeNode root = new TreeNode(pre[ps]);
        int mid = idx.get(pre[ps]);
        int leftSize = mid - is;
        root.left = build(pre, ps + 1, ps + leftSize, in, is, mid - 1, idx);
        root.right = build(pre, ps + leftSize + 1, pe, in, mid + 1, ie, idx);
        return root;
    }

    @Test
    @DisplayName("trick is use built in python min/max values float(\"inf\"), \"-inf\", as parameters; iterative in-order traversal, check each val is greater than prev")
    public void validateBinarySearchTree() {
        TreeNode root = new TreeNode(2);
        root.left = new TreeNode(1);
        root.right = new TreeNode(3);
        assertTrue(validBst(root, Long.MIN_VALUE, Long.MAX_VALUE));
    }

    private boolean validBst(TreeNode root, long min, long max) {
        if (root == null) return true;
        if (root.val <= min || root.val >= max) return false;
        return validBst(root.left, min, root.val) && validBst(root.right, root.val, max);
    }

    @Test
    @DisplayName("non-optimal store tree in sorted array; iterative dfs in-order and return the kth element processed, go left until null, pop, go right once")
    public void kthSmallestElementInABST() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.right = new TreeNode(2);
        Deque<TreeNode> stack = new ArrayDeque<>();
        int k = 1;
        TreeNode cur = root;
        while (true) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            if (--k == 0) break;
            cur = cur.right;
        }
        assertEquals(1, cur.val);
    }

    @Test
    @DisplayName("compare p, q values to curr node, base case: one is in left, other in right subtree, then curr is lca")
    public void lowestCommonAncestorOfBST() {
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2); root.right = new TreeNode(8);
        root.left.left = new TreeNode(0); root.left.right = new TreeNode(4);
        TreeNode p = root.left, q = root.right;
        TreeNode cur = root;
        while (cur != null) {
            if (p.val < cur.val && q.val < cur.val) cur = cur.left;
            else if (p.val > cur.val && q.val > cur.val) cur = cur.right;
            else break;
        }
        assertEquals(6, cur.val);
    }

    @Test
    @DisplayName("node has children characters, and bool if its an ending character, node DOESN’T have or need char, since root node doesn’t have a char, only children")
    public void implementTriePrefixTree() {
        Trie trie = new Trie();
        trie.insert("apple");
        assertTrue(trie.search("apple"));
        assertFalse(trie.search("app"));
        assertTrue(trie.startsWith("app"));
    }

    @Test
    @DisplayName("if char = \".\" run search for remaining portion of word on all of curr nodes children")
    public void addAndSearchWord() {
        WordDictionary dict = new WordDictionary();
        dict.addWord("bad"); dict.addWord("dad"); dict.addWord("mad");
        assertFalse(dict.search("pad"));
        assertTrue(dict.search("bad"));
        assertTrue(dict.search(".ad"));
        assertTrue(dict.search("b.."));
    }

    @Test
    @DisplayName("trick: I though use trie to store the grid, reverse thinking, instead store dictionary words, dfs on each cell, check if cell's char exists as child of root node in trie, if it does, update currNode, and check neighbors, a word could exist multiple times in grid, so don’t add duplicates")
    public void wordSearchII() {
        char[][] board = {{'o','a','a','n'},{'e','t','a','e'},{'i','h','k','r'},{'i','f','l','v'}};
        String[] words = {"oath","pea","eat","rain"};
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) cur = cur.children.computeIfAbsent(c, k -> new TrieNode());
            cur.end = true;
        }
        Set<String> found = new HashSet<>();
        for (int r = 0; r < board.length; r++) for (int c = 0; c < board[0].length; c++) dfsWordSearchII(board, r, c, root, "", found);
        assertTrue(found.contains("oath"));
        assertTrue(found.contains("eat"));
    }

    private void dfsWordSearchII(char[][] board, int r, int c, TrieNode node, String path, Set<String> found) {
        if (r < 0 || c < 0 || r == board.length || c == board[0].length || board[r][c] == '#') return;
        char ch = board[r][c];
        TrieNode next = node.children.get(ch);
        if (next == null) return;
        String word = path + ch;
        if (next.end) found.add(word);
        board[r][c] = '#';
        dfsWordSearchII(board, r + 1, c, next, word, found);
        dfsWordSearchII(board, r - 1, c, next, word, found);
        dfsWordSearchII(board, r, c + 1, next, word, found);
        dfsWordSearchII(board, r, c - 1, next, word, found);
        board[r][c] = ch;
    }

    @Test
    @DisplayName("we always want the min of the current frontier, we can store frontier in heap of size k for efficient pop/push; divide and conquer merging lists")
    public void mergeKSortedListsUsingHeap() {
        ListNode[] lists = {list(1,4,5), list(1,3,4), list(2,6)};
        PriorityQueue<ListNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode node : lists) if (node != null) pq.offer(node);
        ListNode dummy = new ListNode(0), tail = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            tail.next = node;
            tail = tail.next;
            if (node.next != null) pq.offer(node.next);
        }
        assertEquals(1, dummy.next.val);
    }

    @Test
    @DisplayName("minheap that’s kept at size k, if its bigger than k pop the min, by the end it should be left with k largest")
    public void topKFrequentElements() {
        int[] nums = {1,1,1,2,2,3};
        int k = 2;
        Map<Integer, Integer> freq = new HashMap<>();
        for (int n : nums) freq.put(n, freq.getOrDefault(n, 0) + 1);
        PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.comparingInt(freq::get));
        for (int n : freq.keySet()) {
            heap.offer(n);
            if (heap.size() > k) heap.poll();
        }
        Set<Integer> set = new HashSet<>(heap);
        assertEquals(new HashSet<>(Arrays.asList(1,2)), set);
    }

    @Test
    @DisplayName("maintain curr median, and all num greater than med in a minHeap, and all num less than med in a maxHeap, after every insertion update median depending on odd/even num of elements")
    public void findMedianFromDataStream() {
        MedianFinder mf = new MedianFinder();
        mf.addNum(1);
        mf.addNum(2);
        assertEquals(1.5, mf.findMedian(), 0.0001);
        mf.addNum(3);
        assertEquals(2.0, mf.findMedian(), 0.0001);
    }

    @Test
    @DisplayName("find the kth largest element in an array")
    public void findKthLargestElement() {
        // use priority queue
        // PriorityQueue is implemented using a binary heap, so elements are reordered internally based on priority, not insertion order.
        int[] numbers = {3, 2, 1, 5, 6, 4};
        int k = 2;


    }

    @Test
    @DisplayName("swap two numbers using 4 different ways")
    public void swapTwoNumbers() {
        // Option 1
        int a = 5, b = 10;
        int temp = a;
        a = b;
        b = temp;
        System.out.println("Option 1 : " + a + " " + b);

        // Option 2
        a = 5; b = 10;
        a = a + b; // a = 15
        b = a - b; // b = 15 - 10 = 5
        a = a - b; // a = 15 - 5 = 10
        System.out.println("Option 2 : " + a + " " + b);

        // Option 3
        a = 5; b = 10;
        a = a * b; // a = 50
        b = a / b; // b = 50 / 10 = 5
        a = a / b; // a = 50 / 5 = 10
        System.out.println("Option 3 : " + a + " " + b);

        // Option 4
        a = 5; b = 10;
        a = a ^ b; // a = 15 (1111)
        b = a ^ b; // b = 5 (0101)
        a = a ^ b; // a = 10 (1010)
        System.out.println("Option 4 : " + a + " " + b);

        // Option 5
        a = 5; b = 10;
        a = (a + b) - (b = a); // a = 10, b = 5
        System.out.println("Option 5 : " + a + " " + b);

    }

    @Test
    @DisplayName("find if 2 strings are anagrams")
    public void stringAnagrams() {

        String first = "LISTEN";
        String second = "SILENT";

        if (first == null || second == null) {
            System.out.println("Both strings should not be null");
        } else if (first.length() != second.length()) {
            System.out.println("Both strings should have same length");
        } else {
            String sortedFristString = Arrays.stream(first.split("")).sorted().filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.joining());
            String sortedSecondString = Arrays.stream(second.split("")).sorted().filter(i -> StringUtils.isNotBlank(i)).collect(Collectors.joining());

            System.out.println("first = " + first + ", second = " + second);
            System.out.println("SortedFirstString = " + sortedFristString + ", SortedSecondString = " + sortedSecondString);
            System.out.println("Are both anagrams : " + sortedFristString.equalsIgnoreCase(sortedSecondString));
        }

    }

    @Test
    @DisplayName("given an array and a target, find any pair of elements whose sum is the target.")
    public void twoSumProblem() {
        int[] arr = {2, 7, 11, 15};
        int target = 9;

        // complexity O(n^2)
        outerLoop:
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] + arr[j] == target) {
                    System.out.println("Pair found : " + arr[i] + " + " + arr[j] + " = " + target);
                    break outerLoop;
                }
            }
        }
        System.out.println("No pair found that sums to the target.");

        // complexity O(n)
        // 2, 7, 11, 8, 15
        // target = 10
        int[] numbers = {2, 7, 11, 8, 15};
        target = 10;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            int complement = target - numbers[i];
            if (map.containsKey(complement)) {
                System.out.println("Pair found : " + complement + " + " + numbers[i] + " = " + target);
                break;
            }
            map.put(numbers[i], complement);
        }
    }

    @Test
    @DisplayName("Given an integer array nums, rotate the array to the right by k steps, where k is non-negative.")
    public void rotateArray() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7};
        int start = 0, end = 6;

        // 2 pointer approach. O(n) time complexity and O(1) space complexity
        while(start < end) {
            int temp = numbers[start];
            numbers[start] = numbers[end];
            numbers[end] = temp;
            start ++;
            end --;
        }
        System.out.println("Reversed array : " + Arrays.toString(numbers));
    }
}
