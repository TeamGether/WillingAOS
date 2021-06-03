import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.teamgether.willing.Fragment.ProfileChallengeDoingFragment
import com.teamgether.willing.Fragment.ProfileChallengeFinishFragment

private const val NUM_PAGES = 2

class ProfileChallengePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NUM_PAGES
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProfileChallengeDoingFragment()
            1 -> ProfileChallengeFinishFragment()
            else -> ProfileChallengeDoingFragment()
        }
    }
}

