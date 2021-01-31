import { Post } from './post';

export class User {
  id: number;
  name: string;
  username: string;
  email: string;
  password: string;
  bio: string;
  posts: Post[];
  likedPosts: Post[];
}
