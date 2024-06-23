package com.example.app.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.data.local.PhotoDaoEntity
import com.example.app.databinding.FragmentHomeBinding
import com.example.app.view.MainActivity
import com.example.app.view.detail.DetailFragment
import com.example.app.view.detail.FullFragment
import com.example.app.view.utils.UiState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var bookmarkRvAdapter: BookmarkRvAdapter
    private lateinit var recentRvAdapter: RecentRvAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private var isLoading = false
    private var lastVisibleItem = 0
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)

        // 어댑터 초기화 및 설정
        setupAdapters()

        observers()
        homeViewModel.getPhotos(currentPage)
        homeViewModel.getBookmarkPhotos()
//        addSampleData()

        return binding.root
    }

    private fun setupAdapters() {
        bookmarkRvAdapter = BookmarkRvAdapter().apply {
            setItemClickListener(object : OnItemClickListener {
                override fun onItemClick(id: String) {
                    (requireActivity() as MainActivity).replaceFragmentWithBackstack(
                        DetailFragment(id)
                    )
                }
            })
        }

        recentRvAdapter = RecentRvAdapter().apply {
            setItemClickListener(object : OnItemClickListener {
                override fun onItemClick(id: String) {
                    FullFragment(id).show(
                        parentFragmentManager, "SampleDialog"
                    )
                }
            })
        }

        binding.rvBookmark.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvBookmark.adapter = bookmarkRvAdapter

        binding.rvRecentImage.adapter = recentRvAdapter
    }

    private fun setupScrollListener() {
        binding.rvRecentImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                Log.d("Home", layoutManager.childCount.toString())
                Log.d("Home", totalItemCount.toString())
                Log.d("Home", lastVisibleItem.toString())

                if (lastVisibleItem > 0) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 4)) {
                        Log.d("TAG", isLoading.toString())
                        // 다음 페이지 데이터를 로드
                        currentPage++
                        homeViewModel.getPhotos(currentPage)
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun observers() {
        homeViewModel.photoState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    Log.d("HomeFragment", "사진 로딩 실패")
                    isLoading = false
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    if (currentPage == 1) {
                        recentRvAdapter.setData(it.data)  // 초기 로드
                        setupScrollListener()  // 초기 로드 후 스크롤 리스너 설정
                    } else {
                        recentRvAdapter.addData(it.data)  // 추가 로드
                    }
                    isLoading = false
                }
            }
        }
        homeViewModel.bookmarkState.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Failure -> {
                    Log.d("HomeFragment", "북마크 로딩 실패")
                }

                is UiState.Loading -> {}
                is UiState.Success -> {
                    bookmarkRvAdapter.setData(it.data)
                }
            }
        }
    }

    private fun addSampleData() {
//        homeViewModel.deleteAllBookmarks()
        // 샘플 데이터 추가
        homeViewModel.addBookmark(
            PhotoDaoEntity(
                "ZjquxEgXheg",
                "https://images.unsplash.com/photo-1718489211836-65a20ad6bd8d?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MjAyNzh8MHwxfGFsbHx8fHx8fHx8fDE3MTg5NTEzMTV8&ixlib=rb-4.0.3&q=80&w=200"
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}