import { Post } from './post';

export class User {
  id: number;
  name: string;
  username: string;
  email: string;
  password: string;
  bio: string;
  createdDate: Date;
  posts: Post[];
  likedPosts: Post[];
}
